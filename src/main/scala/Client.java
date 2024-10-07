import java.io.*;
import java.net.*;
import java.util.Scanner;


    public class Client {
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private String username;
        private Scanner scanner;
    
        public Client(String address, int port, String username) {
            try {
                this.username = username;
                this.socket = new Socket(address, port);
                this.dis = new DataInputStream(socket.getInputStream());
                this.dos = new DataOutputStream(socket.getOutputStream());
                this.scanner = new Scanner(System.in);
    

                dos.writeUTF(username);
    

                new Thread(new Listener()).start();
    

                while (true) {
                    System.out.println("Seleccione una opción:\n1. Enviar mensaje de texto\n2. Enviar nota de voz\n3. Realizar llamada\n4. Crear grupo");
                    String option = scanner.nextLine();
                    handleUserInput(option);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleUserInput(String option) throws IOException {
            switch (option) {
                case "1":
                    System.out.println("Ingrese el destinatario (usuario o grupo):");
                    String recipient = scanner.nextLine();
                    System.out.println("Ingrese su mensaje:");
                    String message = scanner.nextLine();
                    dos.writeUTF("MESSAGE:" + recipient + ":" + message);
                    break;
                case "2":
                    System.out.println("Ingrese el destinatario para la nota de voz:");
                    recipient = scanner.nextLine();
                    sendVoiceMessage(recipient);
                    break;
                case "3":
                    System.out.println("Ingrese el destinatario para la llamada:");
                    recipient = scanner.nextLine();
                    System.out.println("Realizando llamada a " + recipient);
                    break;
                case "4":
                    System.out.println("Ingrese el nombre del nuevo grupo:");
                    String groupName = scanner.nextLine();
                    System.out.println("Ingrese los miembros separados por comas:");
                    String members = scanner.nextLine();
                    dos.writeUTF("CREATE_GROUP:" + groupName + ":" + members);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    
        private void sendVoiceMessage(String recipient) {
            try {
                ClientAudioHandler audioHandler = new ClientAudioHandler(socket.getInetAddress().getHostAddress(), 1235);
                audioHandler.start(); 
               
                Thread.sleep(100); 
                dos.writeUTF("AUDIO_MESSAGE:" + recipient); 
        
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        class Listener implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = dis.readUTF();
                        System.out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese la dirección del servidor:");
            String address = scanner.nextLine();
            System.out.println("Ingrese su nombre de usuario:");
            String username = scanner.nextLine();
            new Client(address, 1234, username);
        }
    }
 
