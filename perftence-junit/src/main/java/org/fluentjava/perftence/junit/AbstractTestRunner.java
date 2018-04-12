package org.fluentjava.perftence.junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.fluentjava.perftence.TestFailureNotifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestRunner extends BlockJUnit4ClassRunner implements TestFailureNotifier {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTestRunner.class);
    private EachTestNotifier notifier;

    public AbstractTestRunner(final Class<?> clazz) throws InitializationError {
        super(clazz);
        tryToInjectFailureNotifier(clazz);
    }

    private void tryToInjectFailureNotifier(final Class<?> clazz) {
        try {
            injectFailureNotifier(clazz.getMethod(failureNotifierMethod(), TestFailureNotifier.class));
        } catch (SecurityException e) {
            handleException(e);
        } catch (NoSuchMethodException e) {
            handleException(e);
        }
    }

    private static String failureNotifierMethod() {
        return "failureNotifier";
    }

    private static void handleException(final Exception e) {
        log().debug(notAbleToInjectTestFailureNotifier(), e.getMessage());
    }

    private static Logger log() {
        return LOG;
    }

    private static String notAbleToInjectTestFailureNotifier() {
        return "Not able to inject failure notifier {}. "
                + "This might be okey for functional/conventional JUnit tests. "
                + "To be able to define performance tests fluently, " + "please extend your test class from "
                + AbstractMultiThreadedTest.class.getName() + " class.";
    }

    private void injectFailureNotifier(final Method failureNotifier) {
        try {
            failureNotifier.invoke(null, this);
        } catch (IllegalArgumentException e) {
            handleException(e);
        } catch (IllegalAccessException e) {
            handleException(e);
        } catch (InvocationTargetException e) {
            handleException(e);
        }
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return computeTestMethods(super.computeTestMethods());
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        final EachTestNotifier eachNotifier = makeNotifier(method, notifier);
        if (method.getAnnotation(Ignore.class) != null) {
            runIgnored(eachNotifier);
        } else {
            runNotIgnored(method, eachNotifier);
        }
    }

    private void runNotIgnored(final FrameworkMethod method, final EachTestNotifier eachNotifier) {
        this.notifier = eachNotifier;
        runTestMethod(method, eachNotifier);
        this.notifier = null;
    }

    private void runTestMethod(final FrameworkMethod method, final EachTestNotifier eachNotifier) {
        eachNotifier.fireTestStarted();
        try {
            methodBlock(method).evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    @Override
    public void testFailed(final Throwable t) {
        if (this.notifier != null) {
            synchronized (this.notifier) {
                this.notifier.addFailure(t);
            }
        }
    }

    private static void runIgnored(final EachTestNotifier eachNotifier) {
        eachNotifier.fireTestIgnored();
    }

    private EachTestNotifier makeNotifier(final FrameworkMethod method, final RunNotifier notifier) {
        return new EachTestNotifier(notifier, describeChild(method));
    }

    @Override
    @Deprecated
    /**
     * classes with no tests are allowed because we might filter some of them out.
     */
    protected void validateInstanceMethods(final List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);
    }

    private List<FrameworkMethod> computeTestMethods(final List<FrameworkMethod> methodList) {
        return processedTestMethods(new ArrayList<>(methodList));
    }

    private List<FrameworkMethod> processedTestMethods(final List<FrameworkMethod> methods) {
        processTestMethods(methods);
        return methods;
    }

    /**
     * Here the extending class can effect which methods (or all) will be executed.
     */
    protected abstract void processTestMethods(final List<FrameworkMethod> methods);

}
