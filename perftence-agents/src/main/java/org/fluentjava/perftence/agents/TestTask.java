package org.fluentjava.perftence.agents;

public interface TestTask {

    /**
     * When task is scheduled to be run
     */
    Time when();

    /**
     * Next task to be scheduled if any, this method guaranteed to be run in the
     * same thread as the run -method
     * 
     * @return next task
     */
    TestTask nextTaskIfAny();

    void run(final TestTaskReporter reporter) throws Exception;

    TestTaskCategory category();

}