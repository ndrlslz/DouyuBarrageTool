package service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * createdTime 2015/12/23
 *
 * @author ndrlslz
 */
public class HttpClient {
    private CloseableHttpClient client;

    public HttpClient() {
        client = HttpClients.createDefault();
    }

    public String doGet(String url) {
        HttpUriRequest httpGet = new HttpGet(url);
        return request(httpGet);
    }

    public String doPost(String url) {
        HttpUriRequest httpPost = new HttpPost(url);
        return request(httpPost);
    }

    private String request(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}