import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientHandler(Socket socket, String username, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.username = username;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                message = dis.readUTF();
                System.out.println(username + ": " + message);
                Server.sendMessage(username, "group1", message); // Replace with actual recipient/group
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendMessage(Object messageContent) {
        try {
            dos.writeUTF(messageContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
