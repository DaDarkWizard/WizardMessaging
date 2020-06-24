package dadarkwizard.server.messages;

import java.nio.ByteBuffer;

public enum MessageType {
    UNKNOWN_TYPE((byte) 0x00, Message.class),
    CONNECTION_REQUEST((byte) 0x01, ConnectionRequestMessage.class),
    FATAL_ERROR((byte) 0x02, FatalErrorMessage.class);

    private byte id;
    private Class<?> messageClass;

    public byte getTypeId() {
        return id;
    }

    public Class<?> getMessageClass() {
        return messageClass;
    }

    MessageType(byte id, Class messageClass) {
        this.messageClass = messageClass;
        this.id = id;
    }

    public static MessageType getMessageType(byte id) {
        switch (id) {
            case 0x01:
                return CONNECTION_REQUEST;
            case 0x02:
                return FATAL_ERROR;
            default:
                return UNKNOWN_TYPE;
        }
    }

}


