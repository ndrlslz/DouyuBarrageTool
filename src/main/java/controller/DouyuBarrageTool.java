package controller;

import service.Config;

import java.io.IOException;

/**
 * 临时方案,起n个线程获取n个gid的弹幕
 *
 * @author ndrlslz
 */
public class DouyuBarrageTool {
    public static void getBarrage() throws IOException {
        new Thread(new BarrageThread()).start();
    }
}
