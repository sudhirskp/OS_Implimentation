package Thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ConditionalVar {

    public static void main(String[] args) {
        Object lock = new Object();
        AtomicInteger value = new AtomicInteger(0);
        AtomicBoolean flag = new AtomicBoolean(false);

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                while (value.get() != 5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread 1: " + value.get());
                flag.set(false); // reset the flag after printing the value
                lock.notifyAll();
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                while (flag.get()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
                value.set(5);
                flag.set(true);
                lock.notifyAll();
                System.out.println("Thread 2: " + value.get());
            }
        });

        thread1.start();
        thread2.start();
    }
}