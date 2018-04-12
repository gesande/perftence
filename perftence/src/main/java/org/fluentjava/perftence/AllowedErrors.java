package org.fluentjava.perftence;

import java.util.ArrayList;
import java.util.List;

public final class AllowedErrors {
    private final List<Class<?>> allowedErrors = new ArrayList<>();

    public <ERROR extends Error> void allow(final Class<ERROR> clazz) {
        allowed().add(clazz);
    }

    private List<Class<?>> allowed() {
        return this.allowedErrors;
    }

    public <ERROR extends Error> boolean isAllowed(final ERROR error) {
        return this.allowedErrors.contains(error.getClass());
    }

}