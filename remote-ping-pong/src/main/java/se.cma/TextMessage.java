package se.cma;

public interface TextMessage {
     static String getMessagePingPong(String message, String name, int hint) {
        StringBuilder builder = new StringBuilder();
        builder.append(message).append("-").append(name).append(hint);
        return builder.toString();
    }
}
