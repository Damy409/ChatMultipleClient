public class Message {
    private String sender;
    private String recipient;
    private String content;

    public Message(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    @Override
    public String toString() {
        return sender + " to " + recipient + ": " + content;
    }
}
