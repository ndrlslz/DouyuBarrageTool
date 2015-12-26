package service;

import domain.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * createdTime 2015/12/22
 *
 * @author ndrlslz
 */
public class SendMessage {
    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private String groupId;

    public SendMessage(OutputStream outputStream, String groupId) {
        this.outputStream = outputStream;
        this.groupId = groupId;
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

    public void send() throws IOException {
        String roomId = Config.getRoomId();

        if (!roomId.matches("[0-9]+")) {
            roomId = RoomInformation.getRoomIdByNickname(roomId);
        }
        sendMessage("type@=loginreq/username@=auto_KRLJbE8mZM/password@=1234567890123456/roomid@=" + roomId + "/");
        sendMessage("type@=joingroup/rid@=" + roomId + "/gid@=" + groupId + "/");

        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
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
