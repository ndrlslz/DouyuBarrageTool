package controller;

import service.SendMessage;

import java.io.IOException;
import java.net.Socket;

/**
 * 发送心跳包,防止server端中断连接
 *
 * @author ndrlslz
 */
public class KeepLiveThread implements Runnable {
    private Socket socket;

    public KeepLiveThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        while (socket != null && socket.isConnected()) {
            try {
                SendMessage sendMessage = new SendMessage(socket.getOutputStream(), null);
                sendMessage.sendKeepLive();
                Thread.sleep(60000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
