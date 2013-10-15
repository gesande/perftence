package net.sf.perftence.agents;

public interface TaskSchedulingStatisticsProvider {

	int scheduledTasks();

	Time lastTaskScheduledToBeRun();

}
