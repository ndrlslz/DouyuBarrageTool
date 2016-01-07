package controller;

import org.apache.commons.codec.digest.DigestUtils;
import service.RoomInformation;

import java.io.IOException;
import java.util.UUID;

/**
 * createdTime 2015/12/22
 *
 * @author ndrlslz
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        DouyuBarrageTool.getBarrage();
//        RoomInformation roomInformation = new RoomInformation();
//        System.out.println("result: " + roomInformation.getGroupId());
    }
}
