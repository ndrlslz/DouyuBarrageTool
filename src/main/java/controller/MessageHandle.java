package controller;

import domain.ServerConfig;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import service.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandle {
    private static final Logger logger = Logger.getLogger(MessageHandle.class.getName());
    private static final String GROUP_ID_REGEX = "type@=setmsggroup/.*?/gid@=(.*?)/";
    private static final String SERVER_CONFIG_REGEX = "server_config\":\"(.*?)\"";
    private static final String ROOM_ID_REGEX = "[0-9]+";
    private static final String BARRAGE_REGEX = "type@=chatmsg/.*/nn@=(.*)/txt@=(.*?)/";
    private static final Pattern BARRAGE_PATTERN = Pattern.compile(BARRAGE_REGEX);
    private static final Pattern GROUP_ID_PATTERN = Pattern.compile(GROUP_ID_REGEX);
    private static final Pattern SERVER_CONFIG_PATTERN = Pattern.compile(SERVER_CONFIG_REGEX);

    public static void handleBarrage() throws IOException {
        InputStream inputStream = DouYuClient.barrageSocket.getInputStream();
        int i;
        byte[] bytes = new byte[1024];
        while (DouYuClient.barrageSocket.isConnected() && (i = inputStream.read(bytes)) != -1) {
            parseBarrage(new String(bytes, 0, i));
        }
    }

    public static void parseBarrage(String response) {
        Matcher matcher = BARRAGE_PATTERN.matcher(response);

        if (matcher.find()) {
            logger.info(matcher.group(1) + ": " + matcher.group(2));
        }
    }

    public static String parseGroupId(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        int i;
        byte[] bytes = new byte[1024];
        while (socket.isConnected() && (i = inputStream.read(bytes)) != -1) {
            Matcher matcher = GROUP_ID_PATTERN.matcher(new String(bytes, 0, i));

            if (matcher.find()) {
                socket.close();
                return matcher.group(1);
            }
        }
        throw new RuntimeException("parse group id failed");
    }

    public static ServerConfig parseServerConfig(String html) throws IOException {
        Matcher matcher = SERVER_CONFIG_PATTERN.matcher(html);

        if (matcher.find()) {
            String json = URLDecoder.decode(matcher.group(1), "utf-8");
            ObjectMapper mapper = new ObjectMapper();
            List<ServerConfig> list = mapper.readValue(json, mapper.getTypeFactory().constructParametricType(ArrayList.class, ServerConfig.class));
            return list.get(0);
        }
        throw new RuntimeException("parse server config failed");
    }

    public static String parseRoomId(String html) {
        String roomId = Config.getRoomId();

        if (!roomId.matches(ROOM_ID_REGEX)) {
            roomId = getRoomIdByNickname(html);
        }
        return roomId;
    }

    public static String getRoomIdByNickname(String html) {
        Document doc = Jsoup.parse(html);
        Element element = doc.select("#feedback_report_button").first();
        String href = element.attr("href");
        if (href != null) {
            return href.split("=")[1];
        }
        throw new RuntimeException("parse room id failed");
    }


}
