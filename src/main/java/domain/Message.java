package domain;

import java.util.Arrays;

/**
 * 向斗鱼服务器发送的消息
 * <p/>
 * message由五部分组成
 * 1.通信协议长度,计算后4个部分的字节长度. 占4个字节
 * 2.第二部分内容设置与第一部分一样,占4个字节
 * 3.请求代码，内容固定,发送给斗鱼的话,内容为0xb1,0x02, 斗鱼返回的代码为0xb2,0x02. 占4个字节
 * 4.发送的具体内容
 * 5.末尾还有1个空字节
 */
public class Message {
    private int[] length;
    private int[] code;
    private int[] magic;
    private String context;
    private int[] end;

    /**
     * 构造消息体.
     * 长度部分,第一个字节可表示256字节的长度,若>=256,利用第二个字节, 第二个字节的0x01 = 256
     * 因为这里向斗鱼发送的连接消息不会超过256,暂时只用第一个字节
     */
    public Message(String context) {
        length = new int[]{calculateLength(context), 0x00, 0x00, 0x00};
        code = new int[]{calculateLength(context), 0x00, 0x00, 0x00};
        magic = new int[]{0xb1, 0x02, 0x00, 0x00};
        this.context = context;
        end = new int[]{0x00};
    }

    public int calculateLength(String context) {
        return 4 + 4 + context.length() + 1;
    }

    public int[] getLength() {
        return length;
    }

    public void setLength(int[] length) {
        this.length = length;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public int[] getMagic() {
        return magic;
    }

    public void setMagic(int[] magic) {
        this.magic = magic;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int[] getEnd() {
        return end;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Message{" +
                "length=" + Arrays.toString(length) +
                ", code=" + Arrays.toString(code) +
                ", magic=" + Arrays.toString(magic) +
                ", context='" + context + '\'' +
                ", end=" + Arrays.toString(end) +
                '}';
    }
}
