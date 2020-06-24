package dadarkwizard.messages;

public abstract class Message {
    int id;
    byte[] bytes;

    public final int getId() {
        return this.id;
    }

    ;

    public final byte[] getBytes() {
        return bytes;
    }

    ;
}
