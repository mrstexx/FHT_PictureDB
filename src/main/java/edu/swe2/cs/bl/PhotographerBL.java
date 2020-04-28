package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.model.Photographer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PhotographerBL {

    private static final Logger LOG = LogManager.getLogger(PhotographerBL.class);
    private static QueryEngine queryEngine = QueryEngine.getInstance();

    public static void savePhotographer(Photographer photographer) {
        try {
            if (isValid(photographer)) {
                queryEngine.savePhotographer(photographer);
            }
        } catch (DataAccessException e) {
            LOG.error(e);
        }
    }

    public static boolean isValid(Photographer photographer) {
        return ((photographer.getLastName() != null && !photographer.getLastName().isEmpty() && photographer.getBirthdate() == null || (photographer.getBirthdate() != null && photographer.getBirthdate().isBefore(LocalDate.now()))));
    }

    public static List<Photographer> getAllPhotographers() {
        return queryEngine.getAllPhotographers();
    }

    public static void removePhotographer(Photographer photographer) {
        try {
            queryEngine.removePhotographer(photographer);
        } catch (DataAccessException e) {
            LOG.error(e);
        }
    }

    public static void updatePhotographer(Photographer photographer) {
        try {
            if (isValid(photographer)) {
                queryEngine.updatePhotographer(photographer);
            }
        } catch (DataAccessException e) {
            LOG.error(e);
        }
    }
}
