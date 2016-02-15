package controller;


import service.RoomInformation;
import service.SendMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取gid线程
 * 1.首先去对应的斗鱼房间抓取页面，取到server_config的字符串
 * 2.进行URL解码,得到一组ip, port
 * 3.向其中任意的一个ip,port发送loginreq包
 *
 * @author ndrlslz
 */
public class GroupIdCallback implements Callable<String> {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public GroupIdCallback(RoomInformation roomInformation) throws IOException {
        socket = new Socket(roomInformation.getServerConfig().getIp(), Integer.parseInt(roomInformation.getServerConfig().getPort()));
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

    }

    public String receive(InputStream inputStream) throws IOException {
        int i;
        byte[] bytes = new byte[1024];
        while (socket != null && socket.isConnected() && (i = inputStream.read(bytes)) != -1) {
            String REGEX = "type@=setmsggroup/.*?/gid@=(.*?)/";
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(new String(bytes, 0, i));

            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    @Override
    public String call() throws Exception {
        SendMessage sendMessage = new SendMessage(outputStream);
        try {
            sendMessage.sendReq();

            return receive(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
