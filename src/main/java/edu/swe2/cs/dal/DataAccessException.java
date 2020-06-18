package edu.swe2.cs.dal;

public class DataAccessException extends Exception {

    /**
     * Constructs a new DataAccessException with the specified detail message and cause
     *
     */
    public DataAccessException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    /**
     * Constructs a new DataAccessException with the specified detail message
     *
     */
    public DataAccessException(String errorMessage) { super(errorMessage); }

    /**
     * Returns the cause of this throwable or null if the cause is nonexistent or unknown
     *
     * @return the cause of this throwable or null if the cause is nonexistent or unknown
     */
    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    /**
     * Returns the detail message string of this throwable
     *
     * @return the detail message string of this Throwable instance (which may be null)
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
