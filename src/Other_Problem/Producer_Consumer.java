package Other_Problem;

import java.util.LinkedList;
import java.util.Queue;

public class Producer_Consumer {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity;

    public Producer_Consumer(int capacity) {
        this.capacity = capacity;
    }

    public void produce(int value) throws InterruptedException {
        synchronized (buffer) {
            while (buffer.size() == capacity) {
                buffer.wait();
            }
            buffer.add(value);
            buffer.notifyAll();
        }
    }

    public int consume() throws InterruptedException {
        synchronized (buffer) {
            while (buffer.isEmpty()) {
                buffer.wait();
            }
            int value = buffer.poll();
            buffer.notifyAll();
            return value;
        }


    }

    // Simple demonstration
    public static void main(String[] args) {
        Producer_Consumer pc = new Producer_Consumer(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 20; i++) {
                    pc.produce(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {}
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 20; i++) {
                    int val = pc.consume();
                    System.out.println("Consumed: " + val);
                    Thread.sleep(150);
}
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        consumer.start();
    }
}