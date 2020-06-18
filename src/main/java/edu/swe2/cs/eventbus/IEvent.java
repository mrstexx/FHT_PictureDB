package edu.swe2.cs.eventbus;

public interface IEvent<T> {

    /**
     * Data to be sent between components
     *
     * @return Generic type of data may be sent
     */
    T getData();
}
