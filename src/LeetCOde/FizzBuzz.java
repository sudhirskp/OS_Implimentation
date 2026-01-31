package LeetCOde;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class FizzBuzz {


        private int n;
        AtomicInteger i;
        Object lock;

        public FizzBuzz(int n) {
            this.n = n;
            i = new AtomicInteger(1);
            lock = new Object();
        }

        // printFizz.run() outputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
            while (i.get() <= n) {
                synchronized (lock) {

                    while (i.get() <= n && (i.get() % 3 == 0 && i.get() % 5 != 0) == false) {
                        lock.wait();
                    }
                    if (i.get() <= n) {

                        printFizz.run();
                        i.set(i.get() + 1);

                    }
                    lock.notifyAll();

                }
            }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
            while (i.get() <= n) {
                synchronized (lock) {

                    while (i.get() <= n && (i.get() % 3 != 0 && i.get() % 5 == 0) == false) {
                        lock.wait();
                    }
                    if (i.get() <= n) {

                        printBuzz.run();
                        i.set(i.get() + 1);

                    }
                    lock.notifyAll();

                }
            }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
            while (i.get() <= n) {
                synchronized (lock) {

                    while (i.get() <= n && (i.get() % 3 == 0 && i.get() % 5 == 0) == false) {
                        lock.wait();
                    }
                    if (i.get() <= n) {

                        printFizzBuzz.run();
                        i.set(i.get() + 1);

                    }
                    lock.notifyAll();

                }
            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber) throws InterruptedException {
            while (i.get() <= n) {
                synchronized (lock) {
                    while (i.get() <= n && !(i.get() % 3 != 0 && i.get() % 5 != 0)) {
                        lock.wait();
                    }

                    if (i.get() <= n) {
                        printNumber.accept(i.get());
                        i.incrementAndGet();
                    }
                    lock.notifyAll();
                }
            }
        }

    public static void main(String[] args) {
        FizzBuzz fb = new FizzBuzz(15);

        Runnable printFizz = () -> System.out.print("fizz"+", ");
        Runnable printBuzz = () -> System.out.print("buzz"+", ");
        Runnable printFizzBuzz = () -> System.out.print("fizzbuzz"+", ");
        java.util.function.IntConsumer printNumber = (x) -> System.out.print(x+", ");

        Thread tFizz = new Thread(() -> {
            try { fb.fizz(printFizz); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread tBuzz = new Thread(() -> {
            try { fb.buzz(printBuzz); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread tFizzBuzz = new Thread(() -> {
            try { fb.fizzbuzz(printFizzBuzz); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread tNumber = new Thread(() -> {
            try { fb.number(printNumber); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        tFizz.start();
        tBuzz.start();
        tFizzBuzz.start();
        tNumber.start();

        try {
            tFizz.join();
            tBuzz.join();
            tFizzBuzz.join();
            tNumber.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
//leetcode 1195