import javax.sound.sampled.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientAudioHandler extends Thread {
    private Socket audioSocket;

    public ClientAudioHandler(String serverAddress, int port) throws IOException {
        this.audioSocket = new Socket(serverAddress, port);
    }

    public void run() {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
            DataLine.Info infoMicrophone = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(infoMicrophone);

            microphone.open(format);
            microphone.start();

            OutputStream outputStream = audioSocket.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[10240];

            int bytesRead;
            while ((bytesRead = microphone.read(buffer, 0, buffer.length)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
                bufferedOutputStream.flush();
            }

            bufferedOutputStream.close();
            microphone.close();
            audioSocket.close();

        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
