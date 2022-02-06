package tn.request;

public class Exceptions {
    public static <E extends Exception> void throwIf(boolean condition, E exception) throws E {
        if (condition) {
            throw exception;
        }
    }

    public static void throwIllegalArgumentIf(boolean condition, String message)
            throws IllegalArgumentException {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void throwIllegalArgumentIf(
            boolean condition, String format, Object... args) throws IllegalArgumentException {
        if (condition) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }
}
