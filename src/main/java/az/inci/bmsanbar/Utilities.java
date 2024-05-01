package az.inci.bmsanbar;

public class Utilities {
    public static String getMessage(Exception e)
    {
        String message;
        Throwable throwable = e;
        while (throwable.getCause() != null)
        {
            throwable = throwable.getCause();
        }
        message = throwable.toString();

        return message;
    }
}
