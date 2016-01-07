package domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

import java.io.Serializable;

/**
 * createdTime 2016/1/6
 *
 * @author ndrlslz
 */


public class ServerConfig implements Serializable{

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("port")
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
