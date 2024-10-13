package BusinessLogic;
import DataModels.Server;
import DataModels.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers = new ArrayList<>();
    private List<Thread> serverThreads = new ArrayList<>();
    private int nrOfServers;
    private Strategy strategy;
    public Scheduler(int nrOfServers, SelectionPolicy selectionPolicy) {
        this.nrOfServers = nrOfServers;
        changeStrategy(selectionPolicy);
        for (int i = 0; i < nrOfServers; i++) {
            Server s = new Server();
            servers.add(s);
            Thread t = new Thread(s);
            serverThreads.add(t);
            t.start();
        }
    }

    public int getCurrentTotalWaitingTime() {
        int peakTime = 0;
        for (Server s : servers) {
            peakTime += s.getWaitingPeriod().get();
        }
        return peakTime;
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        } else if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
     }

    public void stopServers() {
        for (Thread t : serverThreads) {
            t.interrupt();
        }
    }

    public StringBuilder printServerStatus() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < servers.size(); i++) {
            sb.append("Server " + (i + 1) + ": ");
            //System.out.print("Server " + (i + 1) + ": ");
            sb.append(servers.get(i).printServerStatus());
            //servers.get(i).printServerStatus();
        }
        return sb;
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }

    public int getNrOfServers() {
        return nrOfServers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void setNrOfServers(int nrOfServers) {
        this.nrOfServers = nrOfServers;
    }

    public void setStrategy(TimeStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Thread> getServerThreads() {
        return serverThreads;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setServerThreads(List<Thread> serverThreads) {
        this.serverThreads = serverThreads;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
