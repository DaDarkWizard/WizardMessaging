package dadarkwizard.server.messages.encoding;

import java.nio.ByteBuffer;

public class ByteString {
    private byte[] bytes;
    private String string;

    public ByteString(String string) {
        ByteBuffer buffer = ByteBuffer.allocate(2 * string.length());

        for (char c : string.toCharArray()) {
            buffer.putChar(c);
        }

        this.string = string;
        this.bytes = buffer.array();
    }

    public ByteString(byte[] bytes) {
        this.bytes = bytes;
        StringBuilder builder = new StringBuilder();
        var buffer = ByteBuffer.wrap(bytes);
        for (int i = 0; i < bytes.length / 2; i++) {
            builder.append(buffer.getChar());
        }
        string = builder.toString();
    }

    /**
     * Gets the length of the string.
     *
     * @return Length of the string in bytes.
     */
    public short length() {
        return (short) string.length();
    }

    public String getString() {
        return this.string;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
