package Thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Concurrency {
    // Simple concurrency demo: two threads increment a shared counter using synchronization // file summary

    static class Counter {
        private int value = 0; // shared counter value, initialized to 0
        synchronized void increment() { value++; } // synchronized increment to ensure atomic updates
        //without sync it show inconsistance value
        synchronized int get() { return value; } // synchronized getter to read the value safely

    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter(); // create a shared Counter instance
        //AtomicInteger count = new AtomicInteger();

        Runnable task = () -> { // task: increments the counter repeatedly
            for (int i = 0; i < 100_000; i++) counter.increment(); // loop that increments 100,000 times
        }; // end of lambda task



        Thread t1 = new Thread(task, "T1"); // create thread T1 with the task
        Thread t2 = new Thread(task, "T2"); // create thread T2 with the task
        t1.start(); // start thread T1
        t2.start(); // start thread T2
        t1.join(); // wait for T1 to finish
        t2.join(); // wait for T2 to finish
        System.out.println("Counter = " + counter.get());
    }


}
