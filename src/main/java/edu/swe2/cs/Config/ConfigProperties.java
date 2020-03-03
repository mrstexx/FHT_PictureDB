package edu.swe2.cs.Config;

import edu.swe2.cs.DAL.DALFactory;
import edu.swe2.cs.DAL.DBManager;
import edu.swe2.cs.DAL.IDAL;
import edu.swe2.cs.Util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Properties;

public class ConfigProperties {

    private final static Logger LOGGER = LogManager.getLogger(ConfigProperties.class.getName());
    private Properties properties = null;

    public ConfigProperties(String fileName) {
        initialize(fileName);
    }

    private void initialize(String fileName) {
        try {
            if (this.properties == null) {
                this.properties = new Properties();
                String resourcePath = URLBuilder.buildURLString(new String[]{"src", "main", "resources", "edu", "swe2", "cs", fileName});
                File propertiesFile = new File(resourcePath);
                if (propertiesFile.exists()) {
                    this.properties.load(new FileInputStream(propertiesFile));
                }

            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Properties File not found " + fileName);
        } catch (IOException e) {
            LOGGER.error("Error while loading properties: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        if (this.properties != null) {
            return this.properties.getProperty(key);
        } else {
            LOGGER.info("No properties loaded.");
        }
        return null;
    }

    public void setProperty(String key, String value) {
        if (this.properties != null) {
            this.properties.setProperty(key, value);
        } else {
            LOGGER.info("No properties loaded");
        }
    }

    /* TESTING */
    public static void main(String[] args) {
        ConfigProperties configProperties = new ConfigProperties("Config.properties");
        DALFactory dalFactory = new DALFactory(configProperties);
        try {
            IDAL dal = dalFactory.getDAL();
            dal.initialize();
            DBManager dbManager = new DBManager(configProperties);
            Connection con = dbManager.getConnection();
            dbManager.closeConnection(con);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOGGER.error("Failed to load DAL");
        }
    }

}
