package controller;

import java.io.IOException;

public class DouyuBarrageTool {
    public static void getBarrage() throws IOException {
        new Thread(new BarrageThread()).start();
    }
}
