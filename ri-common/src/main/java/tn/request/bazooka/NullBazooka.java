package tn.request.bazooka;

public class NullBazooka extends Bazooka {

    public NullBazooka(boolean condition) {
        super(condition);
    }

    public void thenThrowNullPointerException(String message) {
        throw new NullPointerException(message);
    }

    public void thenThrowNullPointerException() {
        throw new NullPointerException();
    }
}
