package net.sf.perftence;

import java.util.List;

import org.apache.commons.collections.list.SynchronizedList;

public final class AsSynchronized {
	@SuppressWarnings("unchecked")
	public static <E> List<E> list(final List<E> list) {
		return SynchronizedList.decorate(list);
	}

}