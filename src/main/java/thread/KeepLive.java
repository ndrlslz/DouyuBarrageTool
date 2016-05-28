package thread;

import controller.DouYuClient;

import java.io.IOException;

public class KeepLive implements Runnable {
    @Override
    public void run() {
        while (!DouYuClient.barrageSocket.isClosed()) {
            try {
                DouYuClient.sendKeepAlive();
                Thread.sleep(60000);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
