package tn.request.app;

public class InvalidEmailFormatException extends Exception {
  public InvalidEmailFormatException(String message) {
    super(message);
  }
}
