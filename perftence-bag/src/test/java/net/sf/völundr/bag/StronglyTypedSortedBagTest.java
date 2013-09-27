package net.sf.völundr.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.sf.völundr.bag.StronglyTypedSortedBag;

import org.junit.Test;

public class StronglyTypedSortedBagTest {

    @SuppressWarnings("static-method")
    @Test
    public void empty() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSamples().size());
        assertTrue(bag.isEmpty());
        assertNull(bag.findFirst());
        assertNull(bag.findLast());
    }

    @SuppressWarnings("static-method")
    @Test
    public void add() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        bag.add("value1");
        assertEquals(1, bag.uniqueSamples().size());
        assertEquals(1, bag.count("value1"));
        assertFalse(bag.isEmpty());
    }

    @SuppressWarnings("static-method")
    @Test
    public void findFirst() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        bag.add("value1");
        bag.add("value2");
        assertEquals(2, bag.size());
        assertEquals(2, bag.uniqueSamples().size());
        assertEquals(1, bag.count("value1"));
        assertEquals(1, bag.count("value2"));
        final String findFirst = bag.findFirst();
        assertNotNull(findFirst);
        assertEquals("value1", findFirst);
    }

    @SuppressWarnings("static-method")
    @Test
    public void findLast() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        bag.add("value1");
        bag.add("value2");
        assertEquals(2, bag.size());
        assertEquals(2, bag.uniqueSamples().size());
        assertEquals(1, bag.count("value1"));
        assertEquals(1, bag.count("value2"));
        final String findLast = bag.findLast();
        assertNotNull(findLast);
        assertEquals("value2", findLast);
    }

    @SuppressWarnings("static-method")
    @Test
    public void firstBeingLast() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        bag.add("value1");
        assertEquals(1, bag.size());
        assertEquals(1, bag.uniqueSamples().size());
        assertEquals(1, bag.count("value1"));
        final String findFirst = bag.findFirst();
        assertNotNull(findFirst);
        assertEquals("value1", findFirst);
        final String findLast = bag.findLast();
        assertNotNull(findLast);
        assertEquals("value1", findLast);
    }

    @SuppressWarnings("static-method")
    @Test
    public void multiThreadUseOfBag() throws InterruptedException {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .synchronizedTreeBag();
        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                bag.add("value1");
            }
        };
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                bag.add("value2");
            }
        };
        new Thread(runnable1).start();
        new Thread(runnable2).start();
        Thread.sleep(50);
        assertEquals(2, bag.uniqueSamples().size());
        assertEquals(1, bag.count("value1"));
        assertEquals(1, bag.count("value2"));

        final String findFirst = bag.findFirst();
        assertNotNull(findFirst);
        assertEquals("value1", findFirst);

        final String findLast = bag.findLast();
        assertNotNull(findLast);
        assertEquals("value2", findLast);
    }

    @SuppressWarnings("static-method")
    @Test
    public void size() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        assertEquals(0, bag.size());
        bag.add("value1");
        bag.add("value1");
        bag.add("value1");
        bag.add("value1");
        bag.add("value2");
        bag.add("value1");
        bag.add("value2");
        bag.add("value1");
        bag.add("value3");
        bag.add("value1");
        assertEquals(10, bag.size());
        assertEquals(3, bag.uniqueSamples().size());
    }

    @SuppressWarnings("static-method")
    @Test
    public void clear() {
        final StronglyTypedSortedBag<String> bag = StronglyTypedSortedBag
                .treeBag();
        assertEquals(0, bag.size());
        bag.add("value1");
        bag.add("value2");
        bag.add("value3");
        assertEquals(3, bag.size());
        assertEquals(3, bag.uniqueSamples().size());
        bag.clear();
        assertEquals(0, bag.size());
        assertEquals(0, bag.uniqueSamples().size());
    }

}
