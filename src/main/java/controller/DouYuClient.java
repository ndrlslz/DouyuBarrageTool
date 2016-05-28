package controller;

import domain.Message;
import domain.ServerConfig;
import org.apache.http.util.Asserts;
import org.apache.log4j.Logger;
import service.Config;
import service.HttpClient;
import service.MD5Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class DouYuClient {
    private static final Logger logger = Logger.getLogger(DouYuClient.class.getName());
    private static final String KEEP_LIVE = "type@=keeplive/tick@=70/";
    private static final String LOGIN_REQUEST = "type@=loginreq/roomid@=%s/";
    private static final String JOIN_GROUP = "type@=joingroup/rid@=%s/gid@=%s/";
    private static final String ASK_FOR_GROUP_ID = "type@=loginreq/username@=/ct@=2/password@=/roomid@=%s" +
            "/devid@=%s/rt@=%s/vk@=%s/ver@=%s/";
    private static final String VERSION = "20150526";
    private static final String MAGIC = "7oE9nPEG9xXV69phU31FYCLUagKeYtsF";

    public static Socket serverConfigSocket;
    public static Socket barrageSocket;
    private static HttpClient httpClient;

    static {
        try {
            initConnection();
        } catch (IOException e) {
            logger.error("init connection error");
            throw new RuntimeException("init connection failed");
        }
    }

    public static void initConnection() throws IOException {
        logger.debug("start to init connection");

        httpClient = new HttpClient();
        barrageSocket = new Socket(Config.getAddress(), Integer.parseInt(Config.getPort()));
        ServerConfig serverConfig = MessageHandle.parseServerConfig(getRoomInformation());
        serverConfigSocket = new Socket(serverConfig.getIp(), Integer.parseInt(serverConfig.getPort()));
    }

    public static String getRoomInformation() {
        return httpClient.doGet(Config.getDouyu() + "/" + Config.getRoomId());
    }

    public static void sendLoginRequest(String roomId) throws IOException {
        sendMessage(String.format(LOGIN_REQUEST, roomId), barrageSocket);
    }

    public static void sendJoinGroup(String roomId, String groupId) throws IOException {
        sendMessage(String.format(JOIN_GROUP, roomId, groupId), barrageSocket);
    }

    public static void sendAskForGroupId(String roomId) throws IOException {
        String devid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String rt = String.valueOf(System.currentTimeMillis() / 1000);
        String vk = MD5Utils.MD5(rt + MAGIC + devid);

        sendMessage(String.format(ASK_FOR_GROUP_ID, roomId, devid, rt, vk, VERSION), serverConfigSocket);
    }

    public static void sendKeepAlive() throws IOException {
        sendMessage(KEEP_LIVE, barrageSocket);
    }

    public static void sendMessage(String context, Socket socket) throws IOException {
        Asserts.notNull(context, "message");

        Message message = new Message(context);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        writeMessageToStream(message, byteArrayOutputStream);
        socket.getOutputStream().write(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
    }

    public static void writeMessageToStream(Message message, OutputStream outputStream) throws IOException {
        putDataToStream(message.getLength(), outputStream);
        putDataToStream(message.getCode(), outputStream);
        putDataToStream(message.getMagic(), outputStream);
        outputStream.write(message.getContext().getBytes());
        putDataToStream(message.getEnd(), outputStream);
    }

    public static void putDataToStream(int[] data, OutputStream outputStream) throws IOException {
        for (int i : data) {
            outputStream.write(i);
        }
    }
}
