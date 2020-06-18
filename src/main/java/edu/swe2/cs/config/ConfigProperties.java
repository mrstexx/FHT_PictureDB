package edu.swe2.cs.config;

import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.util.SystemProperties;
import edu.swe2.cs.util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConfigProperties {

    private final static Logger LOGGER = LogManager.getLogger(ConfigProperties.class.getName());
    private final static String FILENAME = "Config";
    private static ResourceBundle properties = null;
    private static ConfigProperties configProperties = null;

    private ConfigProperties() {
        initialize();
    }

    private void initialize() {
        if (properties == null) {
            properties = ResourceBundle.getBundle(FILENAME);
        }
    }

    private static void checkConfigProperties() {
        if (properties == null) {
            configProperties = new ConfigProperties();
        }
    }

    /**
     * Get property corresponding to a given key
     *
     * @param key Key with which the specified value is associated
     * @return Property which is mapped to the given key
     */
    public static String getProperty(String key) {
        checkConfigProperties();
        if (properties != null) {
            return properties.getString(key);
        } else {
            LOGGER.info("No properties loaded.");
        }
        return null;
    }
}
