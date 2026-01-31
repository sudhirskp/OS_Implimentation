package LeetCOde;

public class PrintFooBar {


    private int n;
    private boolean flag;
    Object lock;

    public PrintFooBar(int n) {
        this.n = n;
        flag = false;
        lock = new Object();
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        synchronized (lock) {
            for (int i = 0; i < n; i++) {

                // printFoo.run() outputs "foo". Do not change or remove this line.
                while (flag == true) {
                    lock.wait();
                }
                printFoo.run();
                flag = true;
                lock.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        synchronized (lock) {
            for (int i = 0; i < n; i++) {

                // printBar.run() outputs "bar". Do not change or remove this line.
                while (flag == false) {
                    lock.wait();
                }
                printBar.run();
                flag = false;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrintFooBar fooBar = new PrintFooBar(2);

        Thread t1 = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
//leetcode 1115
