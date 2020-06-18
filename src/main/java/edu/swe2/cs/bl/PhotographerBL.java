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

    /**
     * Used to store a photographer by utilizing a query engine
     *
     * @param photographer Photographer to be stored
     */
    public static void savePhotographer(Photographer photographer) {
        try {
            if (isValid(photographer)) {
                queryEngine.savePhotographer(photographer);
            }
        } catch (DataAccessException e) {
            LOG.error(e);
        }
    }

    /**
     * Used to validate a photographer entity
     *
     * @param photographer Photographer to be validated
     * @return True if this photographer contains a non-empty last name and his birth date is either null or before today's date
     */
    public static boolean isValid(Photographer photographer) {
        return ((photographer.getLastName() != null && !photographer.getLastName().trim().isEmpty() && photographer.getBirthdate() == null || (photographer.getLastName() != null && !photographer.getLastName().trim().isEmpty() && photographer.getBirthdate() != null && photographer.getBirthdate().isBefore(LocalDate.now()))));
    }

    /**
     * Get a list of all stored Photographers
     *
     * @return List containing all photographers
     */
    public static List<Photographer> getAllPhotographers() {
        return queryEngine.getAllPhotographers();
    }

    /**
     * Used to remove a photographer entity
     *
     * @param photographer Photographer to be removed
     */
    public static void removePhotographer(Photographer photographer) {
        try {
            queryEngine.removePhotographer(photographer);
        } catch (DataAccessException e) {
            LOG.error(e);
        }
    }

    /**
     * Used to update a photographer
     *
     * @param photographer Photographer to be updated
     */
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