package controller;

import thread.Barrage;
import thread.GroupId;
import thread.KeepLive;

import java.io.IOException;
import java.util.concurrent.*;

public class DouyuBarrageTool {
    public static void getBarrage() throws IOException, ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);

        String roomId = MessageHandle.parseRoomId(DouYuClient.getRoomInformation());
        System.out.println("加入房间: " + roomId);

        String groupId = service.submit(new GroupId(roomId)).get();
        System.out.println("加入group: " + groupId);

        System.out.println("开始获取弹幕");
        System.out.println("----------------------------------------");
        service.submit(new Barrage(roomId, groupId));
        service.submit(new KeepLive());
    }
}
