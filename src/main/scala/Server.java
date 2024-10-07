import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, ClientHandler> clients = new HashMap<>();
    private static Map<String, List<String>> groups = new HashMap<>();
    private static List<Message> messageHistory = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                String username = dis.readUTF();
                System.out.println(username + " connected.");

                ClientHandler clientHandler = new ClientHandler(socket, username, dis, dos);
                clients.put(username, clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createGroup(String groupName, List<String> members) {
        groups.put(groupName, members);
        System.out.println("Group " + groupName + " created with members " + members);
    }

    public static void sendMessage(String sender, String recipient, Object messageContent) {
        if (messageContent instanceof String) {
            Message message = new Message(sender, recipient, (String) messageContent);
            messageHistory.add(message);
            sendToRecipientOrGroup(sender, recipient, message.toString());
        } else if (messageContent instanceof AudioMessage) {
            AudioMessage audioMessage = (AudioMessage) messageContent;
            messageHistory.add(new Message(sender, recipient, "Audio message sent."));
            sendToRecipientOrGroup(sender, recipient, audioMessage);
        }
    }
    
    private static void sendToRecipientOrGroup(String sender, String recipient, Object messageContent) {
        if (clients.containsKey(recipient)) {
            clients.get(recipient).sendMessage(messageContent);
        } else if (groups.containsKey(recipient)) {
            for (String member : groups.get(recipient)) {
                if (!member.equals(sender) && clients.containsKey(member)) {
                    clients.get(member).sendMessage(messageContent);
                }
            }
        }
    }
    
    public static List<Message> getMessageHistory() {
        return messageHistory;
    }
}
