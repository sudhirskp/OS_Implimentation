package Thread;

public class Peterson_Sol {
    private int numberOfProcesses;
    private int[] turn;
    private volatile boolean[] request;
    private volatile boolean[] lock;

    public Peterson_Sol(int numberOfProcesses){
        this.numberOfProcesses = numberOfProcesses;
        turn = new int[numberOfProcesses];
        request = new boolean[numberOfProcesses];
        lock = new boolean[numberOfProcesses];
        for(int i = 0; i < numberOfProcesses; i++){
            turn[i] = 0;
            lock[i] = false;
            request[i] = false;
        }
    }

    public synchronized void requestLock(int processId){
        request[processId] = true;
        turn[processId] = (turn[processId] + 1) % numberOfProcesses;
        while(request[processId] && !lock[processId] && (turn[processId] != turn[processId])){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(request[processId] && !lock[processId] && (turn[processId] == turn[processId])){
            lock[processId] = true;
            notifyAll();
        }
    }

    public synchronized void releaseLock(int processId){
        lock[processId] = false;
    }

    public static void main(String[] args) {
        Peterson_Sol ps = new Peterson_Sol(5);

        for(int i = 0; i < ps.numberOfProcesses; i++) {
            final int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    ps.requestLock(finalI);
                    ps.releaseLock(finalI);
                }
            });
            t.start();
        }
    }
}