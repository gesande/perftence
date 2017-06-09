package net.sf.perftence.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class ScheduledTasks implements TaskSchedulingStatisticsProvider {
    private final List<TestTask> tasks;
    private final Map<TestTask, Long> runTimes;

    ScheduledTasks() {
        this.tasks = Collections.synchronizedList(new ArrayList<TestTask>());
        this.runTimes = Collections.synchronizedMap(new HashMap<TestTask, Long>());
    }

    private List<TestTask> tasks() {
        return this.tasks;
    }

    private Map<TestTask, Long> runTimes() {
        return this.runTimes;
    }

    public void add(final TestTask task) {
        tasks().add(task);
        runTimes().put(task,
                System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(task.when().time(), task.when().timeUnit()));
    }

    public void remove(final TestTask task) {
        tasks().remove(task);
        runTimes().remove(task);
    }

    @Override
    public int scheduledTasks() {
        return tasks().size();
    }

    public boolean hasScheduledTasks() {
        return !tasks().isEmpty();
    }

    @Override
    public Time lastTaskScheduledToBeRun() {
        if (hasScheduledTasks()) {
            synchronized (tasks()) {
                final TestTask key = tasks().get(tasks().size() - 1);
                if (key != null) {
                    final Long runtimeKey = runTimes().get(key);
                    return runtimeKey == null ? null
                            : TimeSpecificationFactory.inMillis(runtimeKey - System.currentTimeMillis());
                }
            }
        }
        return null;
    }
}