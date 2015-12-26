package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 获得roomId,groupId信息
 *
 * @author ndrlslz
 */
public class RoomInformation {
    private static HttpClient httpClient = new HttpClient();


    public static String getRoomIdByNickname(String roomName) {
        String contextHtml = httpClient.doGet(Config.getDouyu() + "/" + roomName);
        return getRoomIdByHtml(contextHtml);
    }

    private static String getRoomIdByHtml(String html) {
        Document doc = Jsoup.parse(html);
        Element element = doc.select("#feedback_report_button").first();
        String href = element.attr("href");
        if (href != null) {
            return href.split("=")[1];
        }
        return null;
    }

    public static void getGroupId() {

    }
}
