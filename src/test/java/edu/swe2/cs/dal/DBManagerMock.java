package edu.swe2.cs.dal;

import java.sql.Connection;

public class DBManagerMock {

    private static DBManagerMock dbManagerMock;

    private DBManagerMock() {
        // empty body
    }

    public static DBManagerMock getInstance() {
        return new DBManagerMock();
    }

    public Connection getConnection() {
        return null;
    }

}
