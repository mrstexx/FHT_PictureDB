package edu.swe2.cs.DAL;

import edu.swe2.cs.Config.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBManager {

    private final static Logger LOGGER = LogManager.getLogger(DBManager.class.getName());
    private String driver = null;
    private ConfigProperties properties = null;
    private String url = null;
    private String user = null;
    private String password = null;
    private Connection connection = null;

    public DBManager(ConfigProperties properties) {
        this.properties = properties;
        initialize();
    }

    private void initialize() {
        this.driver = properties.getProperty("driver");
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
        this.connection = getConnection();
        initializeBaseTables();
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            LOGGER.info("Establishing db connection..");
        } catch (SQLException e) {
            LOGGER.warn("Unable to establish a connection to Database");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Failed to load driver class " + driver);
        }
        return con;
    }

    public void closeConnection(Connection con) {
        try {
            con.close();
            LOGGER.info("Closing db connection..");
        } catch (SQLException e) {
            LOGGER.error("Unable to close connection to database");
        }
    }

    public int execUpdate(String sqlStatement) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlStatement);
        return preparedStatement.executeUpdate();
    }


    private void initializeBaseTables() {
        try {
            createPhotographerTable();
            createPictureTable();
        } catch (SQLException e) {
            LOGGER.error("Failed to create base tables");
        }
    }

    private void createPhotographerTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS photographer(");
        stringBuilder.append("\n");
        stringBuilder.append("id SERIAL PRIMARY KEY,");
        stringBuilder.append("\n");
        stringBuilder.append("firstname TEXT NOT NULL,");
        stringBuilder.append("\n");
        stringBuilder.append("lastname TEXT NOT NULL");
        stringBuilder.append("\n");
        stringBuilder.append(")");
        execUpdate(stringBuilder.toString());
    }

    private void createPictureTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS picture(");
        execUpdate(stringBuilder.toString());
    }
}
