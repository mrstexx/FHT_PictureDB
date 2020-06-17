package edu.swe2.cs.dal;

import edu.swe2.cs.config.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBManager {

    private final static Logger LOGGER = LogManager.getLogger(DBManager.class.getName());
    private static String driver = null;
    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static Connection connection = null;
    private static DBManager dbManager = null;

    private DBManager() {
        initialize();
    }

    private void initialize() {
        driver = ConfigProperties.getProperty("driver");
        url = ConfigProperties.getProperty("url");
        user = ConfigProperties.getProperty("user");
        password = ConfigProperties.getProperty("password");
        connection = getConnection();
        initializeBaseTables();
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
                LOGGER.info("Establishing db connection..");
            } catch (SQLException e) {
                LOGGER.warn("Unable to establish a connection to Database");
            } catch (ClassNotFoundException e) {
                LOGGER.warn("Failed to load driver class " + driver);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
            LOGGER.info("Closing db connection..");
        } catch (SQLException e) {
            LOGGER.error("Unable to close connection to database");
        }
    }

    public void execUpdate(String sqlStatement) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.executeUpdate();
    }


    private void initializeBaseTables() {
        try {
            createPhotographerTable();
            createEXIFTable();
            createIPTCTable();
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
        stringBuilder.append("lastname TEXT NOT NULL,");
        stringBuilder.append("\n");
        stringBuilder.append("firstname TEXT,");
        stringBuilder.append("\n");
        stringBuilder.append("birthdate DATE,");
        stringBuilder.append("\n");
        stringBuilder.append("notes TEXT");
        stringBuilder.append(")");
        execUpdate(stringBuilder.toString());
    }

    private void createPictureTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS picture(");
        stringBuilder.append("\n");
        stringBuilder.append("id SERIAL PRIMARY KEY,");
        stringBuilder.append("\n");
        stringBuilder.append("photographer_id INTEGER,");
        stringBuilder.append("\n");
        stringBuilder.append("exif_id INTEGER UNIQUE,");
        stringBuilder.append("\n");
        stringBuilder.append("iptc_id INTEGER UNIQUE,");
        stringBuilder.append("\n");
        stringBuilder.append("file_name TEXT NOT NULL UNIQUE,");
        stringBuilder.append("\n");
        stringBuilder.append("FOREIGN KEY (photographer_id) REFERENCES photographer(id),");
        stringBuilder.append("\n");
        stringBuilder.append("FOREIGN KEY (exif_id) REFERENCES exif_info(id),");
        stringBuilder.append("\n");
        stringBuilder.append("FOREIGN KEY (iptc_id) REFERENCES iptc_info(id))");
        execUpdate(stringBuilder.toString());
    }

    private void createEXIFTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS exif_info(");
        stringBuilder.append("\n");
        stringBuilder.append("id SERIAL PRIMARY KEY,");
        stringBuilder.append("\n");
        stringBuilder.append("camera TEXT NOT NULL,");
        stringBuilder.append("\n");
        stringBuilder.append("lens TEXT NOT NULL,");
        stringBuilder.append("\n");
        stringBuilder.append("captureDate TIMESTAMP NOT NULL");
        stringBuilder.append("\n");
        stringBuilder.append(")");
        execUpdate(stringBuilder.toString());
    }

    private void createIPTCTable() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS iptc_info(");
        stringBuilder.append("\n");
        stringBuilder.append("id SERIAL PRIMARY KEY,");
        stringBuilder.append("\n");
        stringBuilder.append("title TEXT,");
        stringBuilder.append("\n");
        stringBuilder.append("caption TEXT,");
        stringBuilder.append("\n");
        stringBuilder.append("city TEXT,");
        stringBuilder.append("\n");
        stringBuilder.append("tags TEXT");
        stringBuilder.append("\n");
        stringBuilder.append(")");
        execUpdate(stringBuilder.toString());
    }
}
