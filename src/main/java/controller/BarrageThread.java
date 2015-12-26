package controller;

import org.apache.log4j.Logger;
import service.Config;
import service.SendMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取斗鱼弹幕主线程
 * 获得websocket连接,向斗鱼服务器发送连接数据,并接受服务器返回.
 *
 * @author ndrlslz
 */
public class BarrageThread implements Runnable {
    private static Logger logger = Logger.getLogger(BarrageThread.class.getName());
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean close;
    private String groupId;

    /**
     * 因为暂时取不到gid,所以手动传入gid.
     */
    public BarrageThread(String groupId) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("start to connect to server.");
        }

        socket = new Socket(Config.getAddress(), Integer.parseInt(Config.getPort()));
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        close = false;
        this.groupId = groupId;

    }

    @Override
    public void run() {
        SendMessage sendMessage = new SendMessage(outputStream, groupId);
        try {
            while (!close) {
                if (logger.isDebugEnabled()) {
                    logger.debug("start to send message to server.");
                }

                sendMessage.send();
                new Thread(new KeepLiveThread(socket)).start();

                if (logger.isDebugEnabled()) {
                    logger.debug("start to receive message from server.");
                }
                receive(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void receive(InputStream inputStream) throws IOException {
        int i;
        byte[] bytes = new byte[1024];
        while (socket != null && socket.isConnected() && (i = inputStream.read(bytes)) != -1) {
            parseResponse(new String(bytes, 0, i));
        }

    }

    /**
     * 只解析弹幕，没有解析礼物.
     */
    public void parseResponse(String response) {
        String REGEX = "type@=chatmessage/.*/content@=(.*)/snick@=(.*?)/";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            System.out.println((matcher.group(2) + ": " + matcher.group(1)));
            logger.info(matcher.group(2) + ": " + matcher.group(1));
        }
    }

    public void closeConnection() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
