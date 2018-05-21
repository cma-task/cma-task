package se.cma;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static class PingPong {
        public static final int HIT_GAME = 15;
        public static final String MESSAGE_START_PLAYER = "test";

        public static final int SOCKET_PORT = 8071;

        public static Map<Integer, String> namesPingPongs;
        static {
            namesPingPongs = new HashMap();
            namesPingPongs.put(1, "ping");
            namesPingPongs.put(2, "pong");
        }
    }
}
