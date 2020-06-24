package dadarkwizard.server.exceptions;

public class BadConfigurationException extends RuntimeException {
    public String message;

    public BadConfigurationException(String message) {
        this.message = message;
    }
}
