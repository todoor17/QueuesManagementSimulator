package BusinessLogic;

import DataModels.Server;
import DataModels.Task;
import Utils.EventLog;
import javafx.scene.control.TextArea;

import java.util.*;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minProcessingTime;
    public int maxProcessingTime;
    public int nrOfServers;
    public int nrOfClients;

    private Scheduler scheduler;
    private List<Task> generatedTasks = new ArrayList<>();
    private SelectionPolicy selectionPolicy;
    private TextArea textArea;

    public SimulationManager(SelectionPolicy selectionPolicy, int timeLimit, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, int nrOfServers, int nrOfClients, TextArea textArea) {
        this.selectionPolicy = selectionPolicy;
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.nrOfServers = nrOfServers;
        this.nrOfClients = nrOfClients;
        this.textArea = textArea;
        if (selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            scheduler = new Scheduler(nrOfServers, selectionPolicy);
        } else if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            scheduler = new Scheduler(nrOfServers, selectionPolicy);
        }
        generateNRandomTasks();
        sortTasks();
    }
    private void generateNRandomTasks() {
        for (int i = 0; i < nrOfClients; i++) {
            int taskId = i + 1;
            Random random = new Random();
            int taskArrivalTime = random.nextInt(minArrivalTime, maxArrivalTime) + 1;
            int taskProcessingTime = random.nextInt(minProcessingTime, maxProcessingTime) + 1;
            Task t = new Task(taskId, taskArrivalTime, taskProcessingTime);
            generatedTasks.add(t);
            sortTasks();
        }
    }

    private void sortTasks() {
        Collections.sort(generatedTasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.getArrivalTime(), t2.getArrivalTime());
            }
        });
    }

    private StringBuilder printAvailableClients(int currentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("AVAILABLE CLIENTS at TIME " + currentTime + ": ");
        //System.out.print("AVAILABLE CLIENTS at TIME " + currentTime + ": ");
        for (Task t : generatedTasks) {
            if (t.getArrivalTime() >= currentTime) {
                sb.append("(" + t.getId() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + "), ");
                //System.out.print("(" + t.getId() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + "), ");
            }
        }
        sb.append("\n");
        //System.out.println();
        return sb;
    }

    @Override
    public void run() {
        int currentTime = 0;
        double totalWaitingTime = 0;
        int maxTime = 0;
        int peakTime = 0;

        while (currentTime <= timeLimit) {
            StringBuilder sb = new StringBuilder();
            sb.append("TIME: " + currentTime + "\n");

            for (Server server : scheduler.getServers()) {
                server.setCurrentTime(currentTime);
            }

            sb.append(printAvailableClients(currentTime));
            sb.append(scheduler.printServerStatus());

            int currentTotalWaitingTime = scheduler.getCurrentTotalWaitingTime();
            totalWaitingTime += currentTotalWaitingTime;
            if (currentTotalWaitingTime > maxTime) {
                maxTime = currentTotalWaitingTime;
                peakTime = currentTime;
            }

            Iterator<Task> taskIterator = generatedTasks.iterator();
            while (taskIterator.hasNext()) {
                Task t = taskIterator.next();
                if (t.getArrivalTime() == currentTime + 1) {
                    scheduler.dispatchTask(t);
                    taskIterator.remove();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            currentTime++;

            sb.append("\n" + "Peak time " + peakTime + " with total waiting time " + maxTime + "\n");
            sb.append("Total waiting time =" + totalWaitingTime + ", average waiting time = " + (totalWaitingTime / nrOfClients) + "\n\n");

            textArea.setText(sb.toString());
            EventLog.saveStringBuilderToFile(sb);
        }
        scheduler.stopServers();
    }

}
