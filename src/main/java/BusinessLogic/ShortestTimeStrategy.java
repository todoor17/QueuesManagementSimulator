package BusinessLogic;

import DataModels.Server;
import DataModels.Task;

import java.util.List;

public class ShortestTimeStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        int minWaitingTime = bestServer.getWaitingPeriod().get();

        for (Server server : servers) {
            int waitingTime = server.getWaitingPeriod().get();
            if (waitingTime < minWaitingTime) {
                bestServer = server;
                minWaitingTime = waitingTime;
            }
        }
        bestServer.addTask(t);
    }
}
