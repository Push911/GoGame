package exceptions;


public class NameContainsSpaceException extends Exception {

    public NameContainsSpaceException(String message){
        super(message);
    }
}