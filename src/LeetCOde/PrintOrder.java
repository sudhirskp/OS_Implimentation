package LeetCOde;

import java.util.concurrent.atomic.AtomicInteger;

public class PrintOrder {

    //AtomicInteger v;
    int v ;
    Object lock;

    public PrintOrder() {
       // v = new AtomicInteger(0);
        v = 0;
        lock = new Object();
    }

    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (lock) {
        // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            //v.set(1);
            v = 1;
            lock.notifyAll();

        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (lock) {
            // wait until first has run
            while(v!=1){
                lock.wait();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            //v.set(2);
            v = 2;
            lock.notifyAll();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized(lock){
            // wait until second has run
            while(v!=2){
                lock.wait();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
            //v.set(3);
            v = 3;
            lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        PrintOrder p = new PrintOrder();
       Thread t1 = new Thread(()->{
           try {
               p.first(()->System.out.println("first"));
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       });

       Thread t2 = new Thread(()->{
           try{
               p.second(()->System.out.println("second"));
           }catch (InterruptedException e){
               e.printStackTrace();
           }
       });

        Thread t3 = new Thread(() -> {
            try {
                p.third(() -> System.out.println("third"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // start threads in arbitrary order
       t2.start();
        t3.start();
       t1.start();
    }
}