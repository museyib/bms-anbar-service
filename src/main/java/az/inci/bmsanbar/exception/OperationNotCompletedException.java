package az.inci.bmsanbar.exception;

public class OperationNotCompletedException extends RuntimeException
{
    public OperationNotCompletedException(String message)
    {
        super(message);
    }
}
