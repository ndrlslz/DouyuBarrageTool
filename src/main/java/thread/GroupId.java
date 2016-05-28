package thread;

import controller.DouYuClient;
import controller.MessageHandle;

import java.util.concurrent.Callable;

public class GroupId implements Callable<String> {
    private String roomId;

    public GroupId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String call() throws Exception {
        DouYuClient.sendAskForGroupId(roomId);
        return MessageHandle.parseGroupId(DouYuClient.serverConfigSocket);
    }
}
