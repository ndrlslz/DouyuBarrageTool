package service;

import controller.GroupIdThread;
import domain.ServerConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获得roomId,groupId信息
 *
 * @author ndrlslz
 */
public class RoomInformation {
    private static HttpClient httpClient = new HttpClient();
    private String html;
    private String gid;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public RoomInformation() {
        html = httpClient.doGet(Config.getDouyu() + "/" + Config.getRoomId());

    }

    public String getGroupId() throws IOException, InterruptedException {
        new Thread(new GroupIdThread(this)).start();
        while (true) {
            if (getGid() != null) {
                return gid;
            }

            Thread.sleep(1000); //分配时间给GroupIdThread线程,避免while循环抢占时间
        }
    }

    public ServerConfig getServerConfig() throws IOException {
        String REGEX = "server_config\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            String json = URLDecoder.decode(matcher.group(1), "utf-8");
            ObjectMapper mapper = new ObjectMapper();
            List<ServerConfig> list = mapper.readValue(json, mapper.getTypeFactory().constructParametricType(ArrayList.class, ServerConfig.class));

            return list.get(0);
        }
        return null;
    }

    public String getRoomIdByNickname() {
        Document doc = Jsoup.parse(html);
        Element element = doc.select("#feedback_report_button").first();
        String href = element.attr("href");
        if (href != null) {
            return href.split("=")[1];
        }
        return null;
    }

    public String getRoomId() {
        String roomId = Config.getRoomId();

        if (!roomId.matches("[0-9]+")) {
            roomId = getRoomIdByNickname();
        }
        return roomId;
    }
}
