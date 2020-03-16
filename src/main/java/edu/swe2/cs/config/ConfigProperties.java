package edu.swe2.cs.config;

import edu.swe2.cs.util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {

    private final static Logger LOGGER = LogManager.getLogger(ConfigProperties.class.getName());
    private final static String FILENAME = "Config.properties";
    private static Properties properties = null;
    private static ConfigProperties configProperties = null;

    private ConfigProperties() {
        initialize(FILENAME);
    }

    private void initialize(String fileName) {
        try {
            if (properties == null) {
                properties = new Properties();
                // TODO Leo: Find better way to build URL String. Package name could be changed
                String resourcePath = URLBuilder.buildURLString(new String[]{"src", "main", "resources", "edu", "swe2", "cs", fileName});
                File propertiesFile = new File(resourcePath);
                if (propertiesFile.exists()) {
                    properties.load(new FileInputStream(propertiesFile));
                }

            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Properties File not found " + fileName);
        } catch (IOException e) {
            LOGGER.error("Error while loading properties: " + e.getMessage());
        }
    }

    private static void checkConfigProperties() {
        if (properties == null) {
            configProperties = new ConfigProperties();
        }
    }

    public static String getProperty(String key) {
        checkConfigProperties();
        if (properties != null) {
            return properties.getProperty(key);
        } else {
            LOGGER.info("No properties loaded.");
        }
        return null;
    }

    public static void setProperty(String key, String value) {
        checkConfigProperties();
        if (properties != null) {
            properties.setProperty(key, value);
        } else {
            LOGGER.info("No properties loaded");
        }
    }
}
