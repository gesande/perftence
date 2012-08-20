package net.sf.perftence.fluent;

import java.util.Random;
import java.util.Stack;

import net.sf.perftence.AbstractMultiThreadedTest;
import net.sf.perftence.DefaultTestRunner;
import net.sf.perftence.Executable;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DefaultTestRunner.class)
public class ProduceGood extends AbstractMultiThreadedTest {

    @Test
    public void graphs() {
        final int invocations = 50000;
        final Stack<Integer> stack = new Stack<Integer>();
        setupStack(stack);
        test().setup(
                setup().threads(400).invocations(invocations)
                        .invocationRange(110).throughputRange(10000).build())
                .executable(new Executable() {

                    @Override
                    public void execute() throws Exception {
                        Thread.sleep(stack.pop());
                    }
                }).start();
    }

    private void setupStack(final Stack<Integer> stack) {
        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            stack.push(random(random, 60, 69));
        }
        for (int i = 0; i < 12000; i++) {
            stack.push(random(random, 30, 59));
        }
        for (int i = 0; i < 600; i++) {
            stack.push(random(random, 70, 79));
        }
        for (int i = 0; i < 12000; i++) {
            stack.push(random(random, 30, 59));
        }
        for (int i = 0; i < 300; i++) {
            stack.push(random(random, 80, 89));
        }
        for (int i = 0; i < 12000; i++) {
            stack.push(random(random, 30, 59));
        }
        for (int i = 0; i < 100; i++) {
            stack.push(random(random, 90, 100));
        }
        for (int i = 0; i < 12000; i++) {
            stack.push(random(random, 30, 59));
        }
    }

    private int random(final Random random, final int min, int max) {
        final int i = random.nextInt(max);
        return i < min ? random(random, min, max) : i;
    }
}
