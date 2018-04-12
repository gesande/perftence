package org.fluentjava.perftence.fluent;

import org.fluentjava.perftence.RunNotifier;

public interface InvocationRunner extends RunNotifier {

    public void testFailed(Throwable t);

    public void interruptThreads();

    public void run(Invocation[] runnables);

    public String id();
}
