package controller;

import service.Config;

import java.io.IOException;

/**
 * 临时方案,起n个线程获取n个gid的弹幕
 *
 * @author ndrlslz
 */
public class DouyuBarrageTool {
    private static int[] groups;
    private static int groupCount;

    static {
        groupCount = Integer.parseInt(Config.getGroupCount());
        groups = new int[groupCount];
        for (int i = 1; i <= groupCount; i++) {
            groups[i - 1] = i;
        }
    }


    public static void getBarrage() throws IOException {
        for (int i = 0; i < groupCount; i++) {
            new Thread(new BarrageThread(String.valueOf(groups[i]))).start();
        }
    }
}
