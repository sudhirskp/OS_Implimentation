package LeetCOde;

import javax.swing.text.html.HTMLWriter;
import java.util.concurrent.Semaphore;

public class H2O {
/*
    private int count;
    Object lock;
    public H2O() {
        lock = new Object();
        count = 0;
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {

        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        synchronized(lock){
            while(count==2){
                lock.wait();
            }
            releaseHydrogen.run();
            count++;
            lock.notifyAll();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {

        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        synchronized(lock){
            while(count<2){
                lock.wait();
            }
            releaseOxygen.run();
            count = 0;
            lock.notifyAll();
        }
    }
    */

    Semaphore hydrogen, oxygen;

    public H2O() {
        hydrogen = new Semaphore(2);
        oxygen = new Semaphore(0);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogen.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        oxygen.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygen.acquire(2);
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        hydrogen.release(2);
    }

    public static void main(String[] args) {
        H2O ob = new H2O();

        Thread t1 = new Thread(() -> {
            try {
                        ob.hydrogen(() -> System.out.print("H"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                Thread t2 = new Thread(() -> {
                    try {
                        ob.hydrogen(() -> System.out.print("H"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
            }
                });

                Thread t3 = new Thread(() -> {
                    try {
                        ob.oxygen(() -> System.out.print("O"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
        }
                });

                t1.start();
                t2.start();
                t3.start();
    }
}
//leetCode 1117