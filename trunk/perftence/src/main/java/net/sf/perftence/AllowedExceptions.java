package net.sf.perftence;

import java.util.ArrayList;
import java.util.List;

public final class AllowedExceptions {
    private final List<Class<?>> allowedExceptions = new ArrayList<Class<?>>();

    public <EXCEPTION extends Exception> void allow(final Class<EXCEPTION> clazz) {
        allowed().add(clazz);
    }

    private List<Class<?>> allowed() {
        return this.allowedExceptions;
    }

    public <EXCEPTION extends Exception> boolean isAllowed(final EXCEPTION exception) {
        return this.allowedExceptions.contains(exception.getClass());
    }
}
