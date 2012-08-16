package net.sf.perftence.junit.predicates;

public class Not<T> implements Predicate<T> {
    private final Predicate<T> pred;

    public Not(final Predicate<T> pred) {
        this.pred = pred;
    }

    @Override
    public boolean apply(final T t) {
        return !this.pred.apply(t);
    }
}
