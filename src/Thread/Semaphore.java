package Thread;
import java.util.*;

public class Semaphore {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(3); // 3 permits

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " acquired a permit");
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " released a permit");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
    }


    public void release() {
        synchronized (lock) {
            permits++;
            lock.notifyAll();
        }
    }


    private final Object lock = new Object();
    private int permits;

    public Semaphore(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        synchronized (lock) {
            while (permits <= 0) {
                lock.wait();
            }
            permits--;
        }
    }
}
