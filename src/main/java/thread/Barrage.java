package thread;

import controller.DouYuClient;
import controller.MessageHandle;

import java.io.IOException;

public class Barrage implements Runnable {
    private String roomId;
    private String groupId;

    public Barrage(String roomId, String groupId) {
        this.roomId = roomId;
        this.groupId = groupId;
    }

    @Override
    public void run() {
        try {
            DouYuClient.sendLoginRequest(roomId);
            DouYuClient.sendJoinGroup(roomId, groupId);
            MessageHandle.handleBarrage();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("get barrage failed");
        }

    }
}
