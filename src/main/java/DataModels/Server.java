package DataModels;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private volatile int currentTime = 0;

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
    }

    public void addTask(Task t) {
        tasks.add(t);
        waitingPeriod.addAndGet(t.getServiceTime());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                if (!tasks.isEmpty()) {
                    Task currentTask = tasks.peek();
                    if (currentTask.getArrivalTime() <= currentTime) {
                        int remainingTime = currentTask.getServiceTime();
                        if (remainingTime > 1) {
                            currentTask.setServiceTime(remainingTime - 1);
                            waitingPeriod.decrementAndGet();
                        } else {
                            tasks.poll();
                            waitingPeriod.decrementAndGet();
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public StringBuilder printTasks() {
        StringBuilder sb = new StringBuilder();
        for (Task t : tasks) {
            sb.append("(" + t.getId() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + ") ");
            //System.out.print("(" + t.getId() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + ") ");
        }
        return sb;
    }

    public StringBuilder printServerStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(printTasks());
        //printTasks();
        sb.append(" Total waiting period: " + waitingPeriod.get() + "\n");
        //System.out.println(" Total waiting period: " + waitingPeriod.get());
        return sb;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }
}
