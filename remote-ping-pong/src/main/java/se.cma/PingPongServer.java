package se.cma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class PingPongServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.PingPong.SOCKET_PORT);
            System.out.println("initialized");
            while (true) {
                Socket sock = serverSocket.accept();
                System.out.println(sock.getInetAddress().getHostName()+ " connected");
                Server server = new Server(sock);
                server.start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

class Server extends Thread {
    private PrintStream os;
    private BufferedReader is;
    private InetAddress address;
    public Server(Socket s) throws IOException {
        os = new PrintStream(s.getOutputStream());
        is = new BufferedReader(new InputStreamReader(s.getInputStream()));
        address = s.getInetAddress();
    }
    public void run() {
        int i = 0;
        String message;
        try {
            while ((message = is.readLine()) != null) {
                message = TextMessage.getMessagePingPong(message, Constants.PingPong.namesPingPongs.get(2),++i);
                os.println(message);
                System.out.println(message +"-"+ address.getHostName());
            }
        } catch (IOException e) {
            System.out.println("disconnectServer");
        } finally {
            disconnectServer();
        }
    }

    public void disconnectServer() {
        try {
            System.out.println(address.getHostName()+ " disconnectServer");
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }
}