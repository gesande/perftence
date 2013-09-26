package net.sf.völundr.bag;

import java.util.Collection;

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.bag.TreeBag;

public final class StronglyTypedSortedBag<TYPE> {

    private final SortedBag bag;

    public StronglyTypedSortedBag(final SortedBag bag) {
        this.bag = bag;
    }

    public boolean add(final TYPE value) {
        return bag().add(value);
    }

    public int count(final TYPE value) {
        return bag().getCount(value);
    }

    @SuppressWarnings("unchecked")
    public TYPE findFirst() {
        return (TYPE) (bag().isEmpty() ? null : bag().first());
    }

    @SuppressWarnings("unchecked")
    public TYPE findLast() {
        return (TYPE) (bag().isEmpty() ? null : bag().last());
    }

    @SuppressWarnings("unchecked")
    public Collection<TYPE> uniqueSamples() {
        return bag().uniqueSet();
    }

    private SortedBag bag() {
        return this.bag;
    }

    public static <T> StronglyTypedSortedBag<T> synchronizedTreeBag() {
        return new StronglyTypedSortedBag<T>(
                SynchronizedSortedBag.decorate(new TreeBag()));
    }

    public static <T> StronglyTypedSortedBag<T> treeBag() {
        return new StronglyTypedSortedBag<T>(new TreeBag());
    }

    public long size() {
        return bag().size();
    }

    public boolean isEmpty() {
        return bag().isEmpty();
    }

    public void clear() {
        bag().clear();
    }
}
