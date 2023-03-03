import java.awt.*;
import java.io.Serializable;

class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    public int senderId;
    public int receiverId;
    private final MessageType messageType;
    public long timestamp;

    public Message(int senderId,
                   int receiverId,
                   MessageType messageType,
                   long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
