package LeetCOde;

import java.util.function.IntConsumer;

public class ZeroEvenOddPrint {

        private int n;
        private int curr;
        private int state;
        Object lock;

        public ZeroEvenOddPrint(int n) {
            this.n = n;
            curr = 1;
            state = 0;
            lock = new Object();
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void zero(IntConsumer printNumber) throws InterruptedException {
            while (true) {
                synchronized (lock) {
                    while (state != 0 && curr <= n) {
                        lock.wait();
                    }

                    if (curr > n) {
                        lock.notifyAll();
                        return;
                    }

                    printNumber.accept(0);
                    state = (curr % 2 == 1) ? 1 : 2;
                    lock.notifyAll();
                }
            }
        }

        public void even(IntConsumer printNumber) throws InterruptedException {
            while (true) {
                synchronized (lock) {
                    while (state != 2 && curr <= n) {
                        lock.wait();
                    }

                    if (curr > n) {
                        lock.notifyAll();
                        return;
                    }

                    printNumber.accept(curr);
                    curr++;
                    state = 0;
                    lock.notifyAll();
                }
            }
        }

        public void odd(IntConsumer printNumber) throws InterruptedException {
            while (true) {
                synchronized (lock) {
                    while (state != 1 && curr <= n) {
                        lock.wait();
                    }

                    if (curr > n) {
                        lock.notifyAll();
                        return;
                    }

                    printNumber.accept(curr);
                    curr++;
                    state = 0;
                    lock.notifyAll();
                }
            }
        }

    public static void main(String[] args) {
        ZeroEvenOddPrint ob = new ZeroEvenOddPrint(5);

        Thread t1 = new Thread(() -> {
            try {
                ob.zero((x) -> System.out.print(x));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                ob.even((x) -> System.out.print(x));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                ob.odd((x)->System.out.print(x));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }
}
//leetCode 1116
