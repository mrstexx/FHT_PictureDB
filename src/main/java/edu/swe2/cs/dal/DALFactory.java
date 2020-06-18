package edu.swe2.cs.dal;

import edu.swe2.cs.config.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class DALFactory {

    private final static Logger LOGGER = LogManager.getLogger(DALFactory.class.getName());

    /**
     * Get dal instance of dal class which is specified by this application
     *
     * @return Instance of dal class which is specified by this application or null if class cannot be found
     * @throws Exception if an Exception occurs
     */
    public static IDAL getDAL() throws Exception {
        String dalClass = ConfigProperties.getProperty("DALClass");
        if (dalClass != null) {
            return (IDAL) Class.forName(dalClass).getDeclaredConstructor().newInstance();
        }
        LOGGER.warn("Database Name unknown");
        return null;
    }
}
