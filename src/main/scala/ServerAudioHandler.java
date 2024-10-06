import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAudioHandler extends Thread {
    private ServerSocket audioServerSocket;

    public ServerAudioHandler(int port) throws IOException {
        this.audioServerSocket = new ServerSocket(port);
    }

    public void run() {
        try {
            System.out.println("Servidor de audio escuchando en el puerto: " + audioServerSocket.getLocalPort());
            while (true) {
                Socket socket = audioServerSocket.accept();
                new AudioReceiver(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AudioReceiver extends Thread {
        private Socket socket;

        public AudioReceiver(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
                DataLine.Info infoSpeaker = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(infoSpeaker);

                speaker.open(format);
                speaker.start();

                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[10240];

                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    speaker.write(buffer, 0, bytesRead);
                }

                speaker.drain();
                speaker.close();
                socket.close();

            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }
}
