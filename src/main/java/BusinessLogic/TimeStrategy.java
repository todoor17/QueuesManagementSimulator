package BusinessLogic;

import DataModels.Server;
import DataModels.Task;

import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        int bestServerTime = bestServer.getWaitingPeriod().get();
        for (Server s : servers) {
            if (s.getWaitingPeriod().get() < bestServerTime) {
                bestServer = s;
                bestServerTime = s.getWaitingPeriod().get();
            }
        }
        bestServer.addTask(t);
    }
}
