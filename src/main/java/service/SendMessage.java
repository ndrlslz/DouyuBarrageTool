package service;

import domain.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * createdTime 2015/12/22
 *
 * @author ndrlslz
 */
public class SendMessage {
    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private RoomInformation roomInformation;

    public SendMessage(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.roomInformation = new RoomInformation();
        byteArrayOutputStream = new ByteArrayOutputStream(1000);
    }

    public void sendMessage(String context) throws IOException {
        Message message = new Message(context);
        putDataToStream(message.getLength());
        putDataToStream(message.getCode());
        putDataToStream(message.getMagic());
        byteArrayOutputStream.write(context.getBytes());
        putDataToStream(message.getEnd());

        outputStream.write(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.reset();
    }

    public void send() throws IOException, InterruptedException {

        String roomId = roomInformation.getRoomId();
        System.out.println("进入房间:" + roomId);
        String groupId = roomInformation.getGroupId();
        System.out.println("加入group:" + groupId);
        sendMessage("type@=loginreq/username@=auto_KRLJbE8mZM/password@=1234567890123456/roomid@=" + roomId + "/");
        sendMessage("type@=joingroup/rid@=" + roomId + "/gid@=" + groupId + "/");

        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
    }

    /**
     * 获得groupId的请求
     */
    public void sendReq() throws IOException {
        String roomId = roomInformation.getRoomId();
        String devid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String rt = String.valueOf(System.currentTimeMillis() / 1000);
        String ver = "20150526";
        String magic = "7oE9nPEG9xXV69phU31FYCLUagKeYtsF";
        String vk = MD5Utils.MD5(rt + magic + devid);

        sendMessage("type@=loginreq/username@=/ct@=2/password@=/roomid@=" + roomId +
                "/devid@=" + devid + "/rt@=" + rt + "/vk@=" + vk + "/ver@=" + ver + "/");
    }

    public void sendKeepLive() throws IOException {
        sendMessage("type@=keeplive/tick@=70/");
    }

    /**
     * 将数据放入缓冲流
     */
    public void putDataToStream(int[] data) {
        if (data != null) {
            for (int i : data) {
                byteArrayOutputStream.write(i);
            }
        }
    }


}
