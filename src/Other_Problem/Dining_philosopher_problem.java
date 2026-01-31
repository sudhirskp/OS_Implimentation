package Other_Problem;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Dining_philosopher_problem {
    static class Philosopher implements Runnable {
        private final int id;
        private final int leftIndex;
        private final int rightIndex;
        private final ReentrantLock[] forks;
        private final int rounds;
        private final Random rnd = new Random();

        Philosopher(int id, int leftIndex, int rightIndex, ReentrantLock[] forks, int rounds) {
            this.id = id;
            this.leftIndex = leftIndex;
            this.rightIndex = rightIndex;
            this.forks = forks;
            this.rounds = rounds;
        }

        @Override
        public void run() {
            for (int i = 0; i < rounds; i++) {
                think();
                eatSafely(i + 1);
            }
            System.out.println("Philosopher " + id + " finished.");
        }

        private void think() {
            try {
                Thread.sleep(100 + rnd.nextInt(200));
            } catch (InterruptedException ignored) {}
        }

        private void eatSafely(int round) {
            int first = Math.min(leftIndex, rightIndex);
            int second = Math.max(leftIndex, rightIndex);

            // lock in global order to avoid deadlock
            forks[first].lock();
            try {
                forks[second].lock();
                try {
                    eat(round);
                } finally {
                    forks[second].unlock();
                }
            } finally {
                forks[first].unlock();
            }
        }

        private void eat(int round) {
            System.out.println("Philosopher " + id + " is eating (round " + round + ")");
            try {
                Thread.sleep(100 + rnd.nextInt(200));
            } catch (InterruptedException ignored) {}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int N = 5;
        final int ROUNDS = 5;
        ReentrantLock[] forks = new ReentrantLock[N];
        for (int i = 0; i < N; i++) forks[i] = new ReentrantLock();

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            int left = i;
            int right = (i + 1) % N;
            threads[i] = new Thread(new Philosopher(i, left, right, forks, ROUNDS), "Philosopher-" + i);
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        System.out.println("All philosophers have finished.");
    }
}
