import java.io.Serializable;

public class AudioMessage implements Serializable {
    private String sender;
    private String recipient;
    private byte[] audioData;

    public AudioMessage(String sender, String recipient, byte[] audioData) {
        this.sender = sender;
        this.recipient = recipient;
        this.audioData = audioData;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    @Override
    public String toString() {
        return "Audio message from " + sender + " to " + recipient;
    }
}
