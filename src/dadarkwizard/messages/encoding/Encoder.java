package dadarkwizard.messages.encoding;

import java.nio.ByteBuffer;

public class Encoder {

    private ByteBuffer buffer;

    public Encoder(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes);
    }

    public Encoder(int capacity) {
        buffer = ByteBuffer.allocate(capacity);
    }

    public String getString() {
        short length = buffer.getShort();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(buffer.getChar());
        }
        return builder.toString();
    }

    public char getChar() {
        return buffer.getChar();
    }

    public int getInt() {
        return buffer.getInt();
    }

    public short getShort() {
        return buffer.getShort();
    }

    public byte get() {
        return buffer.get();
    }

    public void putString(String string) {
        buffer.putShort((short) string.length());
        for (char c : string.toCharArray()) {
            buffer.putChar(c);
        }
    }

    public void putShort(short s) {
        buffer.putShort(s);
    }

    public void putChar(char c) {
        buffer.putChar(c);
    }

    public void putInt(int i) {
        buffer.putInt(i);
    }

    public void put(byte b) {
        buffer.put(b);
    }

    public void put(byte[] source) {
        buffer.put(source);
    }
}
