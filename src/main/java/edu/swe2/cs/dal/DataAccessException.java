package edu.swe2.cs.dal;

public class DataAccessException extends Exception{

    public DataAccessException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
