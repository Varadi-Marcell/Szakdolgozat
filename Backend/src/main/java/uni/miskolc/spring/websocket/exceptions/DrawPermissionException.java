package uni.miskolc.spring.websocket.exceptions;

public class DrawPermissionException extends RuntimeException {

    public DrawPermissionException(String errorMessage) {
        super(errorMessage);
    }
}
