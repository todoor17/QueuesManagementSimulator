package BusinessLogic;

import DataModels.Server;
import DataModels.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        Server bestServer = servers.get(0);
        int shortestQueueSize = bestServer.getTasks().size();
        for (Server s : servers) {
            if (s.getTasks().size() < shortestQueueSize) {
                bestServer = s;
                shortestQueueSize = s.getTasks().size();
            }
        }
        bestServer.addTask(t);
    }
}
