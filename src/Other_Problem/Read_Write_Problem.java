package Other_Problem;

public class Read_Write_Problem {
    private int value = 0;
    private int readers = 0;
    private boolean writing = false;

    public int readValue() throws InterruptedException {
        synchronized (this) {
            while (writing) {
                wait();
            }
            readers++;
        }
        try {
            // simulate read
            Thread.sleep(50);
            return value;
        } finally {
            synchronized (this) {
                readers--;
                if (readers == 0) {
                    notifyAll();
                }
            }
        }
    }

    public void writeValue(int v) throws InterruptedException {
        synchronized (this) {
            while (writing || readers > 0) {
                wait();
            }
            writing = true;
        }
        try {
            // simulate write
            Thread.sleep(100);
            value = v;
        } finally {
            synchronized (this) {
                writing = false;
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Read_Write_Problem db = new Read_Write_Problem();

        Thread[] readers = new Thread[3];
        for (int i = 0; i < readers.length; i++) {
            final int id = i + 1;
            readers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        int v = db.readValue();
                        System.out.println("Reader-" + id + " read: " + v);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Reader-" + id);
            readers[i].start();
        }

        Thread[] writers = new Thread[2];
        for (int i = 0; i < writers.length; i++) {
            final int id = i + 1;
            writers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        int newVal = id * 100 + j;
                        db.writeValue(newVal);
                        System.out.println("Writer-" + id + " wrote: " + newVal);
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Writer-" + id);
            writers[i].start();
        }

        try {
            for (Thread t : readers) t.join();
            for (Thread t : writers) t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
