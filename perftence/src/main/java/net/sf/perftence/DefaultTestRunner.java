package net.sf.perftence;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class DefaultTestRunner extends AbstractTestRunner {

    public DefaultTestRunner(final Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected void processTestMethods(final List<FrameworkMethod> methods) {
        // no implementation provided
    }

}
