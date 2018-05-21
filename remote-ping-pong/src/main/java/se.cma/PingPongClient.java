package se.cma;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class PingPongClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        try {
            PingPongClient pingPongClient = new PingPongClient();
            pingPongClient.startConnection(InetAddress.getLocalHost(), Constants.PingPong.SOCKET_PORT);
            pingPongClient.sendMessage();
        } catch (UnknownHostException e) {
            System.out.println("адрес неизвестен");
            System.err.println(e);
        } catch (IOException e) {
            System.err.println("ошибка IOException");
            System.err.println(e);
        } catch (InterruptedException e) {
            System.err.println("поток прерван");
            System.err.println(e);
        }
    }

    public void startConnection(InetAddress ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage() throws IOException, InterruptedException {
        String message = Constants.PingPong.MESSAGE_START_PLAYER;
        for (int i = 1; i <= Constants.PingPong.HIT_GAME; i++) {
            message = TextMessage.getMessagePingPong(message, Constants.PingPong.namesPingPongs.get(1),i);
            out.println(message);
            message = in.readLine();
            System.out.println(message);
            Thread.sleep(1000);
        }
        stopConnection();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}