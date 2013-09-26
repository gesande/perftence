package net.sf.völundr.junit;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import net.sf.völundr.junit.predicates.Not;
import net.sf.völundr.junit.predicates.Predicate;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class JUnitUtilsTest {

    @Collect
    @Present
    @Test
    public void annotionPresent() {
        final List<FrameworkMethod> annotatedAsTest = new TestClass(
                this.getClass()).getAnnotatedMethods(Collect.class);
        assertEquals(2, annotatedAsTest.size());
        final TestSet<Annotation> pred = new TestSet<Annotation>(present());
        JUnitUtils.removeTestMethods(annotatedAsTest, new Not<FrameworkMethod>(
                pred));
        assertEquals(1, annotatedAsTest.size());
    }

    @Collect
    @NotPresent
    @Test
    public void annotionNotPresent() {
        final List<FrameworkMethod> annotatedAsTest = new TestClass(
                this.getClass()).getAnnotatedMethods(Collect.class);
        assertEquals(2, annotatedAsTest.size());
        final TestSet<Annotation> pred = new TestSet<Annotation>(notPresent());
        JUnitUtils.removeTestMethods(annotatedAsTest, new Not<FrameworkMethod>(
                pred));
        assertEquals(1, annotatedAsTest.size());

    }

    @SuppressWarnings({ "unchecked", "static-method" })
    private <T extends Annotation> Class<T> notPresent() {
        return (Class<T>) NotPresent.class;
    }

    @SuppressWarnings({ "unchecked", "static-method" })
    private <T extends Annotation> Class<T> present() {
        return (Class<T>) Present.class;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    @interface Collect { //
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    @interface NotPresent { //
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    @interface Present { //
    }

    private final static class TestSet<T extends Annotation> implements
            Predicate<FrameworkMethod> {

        private final Class<T> testSet;

        public TestSet(final Class<T> testSet) {
            this.testSet = testSet;
        }

        @Override
        public boolean apply(final FrameworkMethod m) {
            T annotation = m.getAnnotation(testSet());
            if (annotation == null) {
                annotation = m.getMethod().getDeclaringClass()
                        .getAnnotation(testSet());
            }
            return annotation != null;
        }

        private Class<T> testSet() {
            return this.testSet;
        }

    }
}
