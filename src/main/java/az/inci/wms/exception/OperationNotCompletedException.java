package az.inci.wms.exception;

public class OperationNotCompletedException extends RuntimeException {
    public OperationNotCompletedException(String message) {
        super(message);
    }
}
