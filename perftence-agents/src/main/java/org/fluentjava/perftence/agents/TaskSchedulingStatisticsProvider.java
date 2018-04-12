package org.fluentjava.perftence.agents;

public interface TaskSchedulingStatisticsProvider {

    int scheduledTasks();

    Time lastTaskScheduledToBeRun();

}
