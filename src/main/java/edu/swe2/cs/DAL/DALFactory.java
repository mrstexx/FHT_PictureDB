package edu.swe2.cs.DAL;

import edu.swe2.cs.Config.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class DALFactory {

    private final static Logger LOGGER = LogManager.getLogger(DALFactory.class.getName());

    public static IDAL getDAL() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String dalClass = ConfigProperties.getProperty("DALClass");
        if (dalClass != null) {
            return (IDAL) Class.forName(dalClass).getDeclaredConstructor().newInstance();
        }
        LOGGER.info("Database Name unknown");
        return null;
    }
}
