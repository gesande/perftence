package org.fluentjava.perftence.junit;

class FullyQualifiedMethodNameWithClassName {

    public String idFor(final Class<?> clazz, final String methodName) {
        return new StringBuilder(clazz.getName()).append(".").append(methodName).toString();
    }
}