package dadarkwizard.messages;


import java.nio.ByteBuffer;

public class ConnectionRequestMessage extends Message {

    public ConnectionRequestMessage() {
        this.id = 1;

    }

    /**
     * @param bytes all bytes used to generate message. Does not include first 5, which are the length of the message
     *              and the message id.
     */
    public ConnectionRequestMessage(byte[] bytes) {
        this.bytes = new byte[bytes.length + 5];
        System.arraycopy(bytes, 0, this.bytes, 5, bytes.length);
        this.bytes[4] = (byte) this.id;
        byte[] length = ByteBuffer.allocate(4).putInt(this.bytes.length).array();
        System.arraycopy(length, 0, this.bytes, 0, 4);


    }

}
