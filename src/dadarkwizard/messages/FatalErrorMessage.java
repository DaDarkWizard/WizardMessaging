package dadarkwizard.messages;

import dadarkwizard.messages.encoding.ByteString;
import dadarkwizard.messages.encoding.Encoder;

public class FatalErrorMessage extends Message {

    private String error;

    public FatalErrorMessage() {
        this.id = 0x02;
    }

    public FatalErrorMessage(String error) {
        id = 0x02;
        bytes = new byte[error.length() + 7];
        var encoder = new Encoder(bytes);
        encoder.putInt(bytes.length);
        encoder.put((byte) id);
        encoder.putString(error);
        this.error = error;
    }

    /**
     * Creates a message from the given array of bytes, minus the size and type
     *
     * @param bytes Bytes for the message, minus the length and type
     */
    public FatalErrorMessage(byte[] bytes) {
        this(bytes, false);
    }


    /**
     * Creates a message from the given array of bytes.
     *
     * @param bytes Bytes for message, minus
     */
    public FatalErrorMessage(byte[] bytes, boolean includeSize) {
        if (includeSize) {
            this.bytes = bytes;
            var encoder = new Encoder(this.bytes);
            encoder.getInt();
            encoder.get();
            this.error = encoder.getString();
        } else {
            this.bytes = new byte[bytes.length + 5];
            System.arraycopy(bytes, 0, this.bytes, 5, bytes.length);
            var encoder = new Encoder(this.bytes);
            encoder.putInt(this.bytes.length);
            encoder.put((byte) id);
            this.error = encoder.getString();
        }
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
        ByteString errorString = new ByteString(error);
        bytes = new byte[error.length() + 7];

        var encoder = new Encoder(bytes);
        encoder.putInt(bytes.length);
        encoder.put((byte) id);
        encoder.putString(error);
    }
}
