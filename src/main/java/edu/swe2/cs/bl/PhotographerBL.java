package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Photographer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PhotographerBL {

    private static final Logger LOG = LogManager.getLogger(PhotographerBL.class);

    public static void savePhotographer(Photographer photographer) {
        try {
            if (isValid(photographer)) {
                Connection connection = DBManager.getInstance().getConnection();
                Objects.requireNonNull(DALFactory.getDAL()).addPhotographer(connection, photographer);
            }
        } catch (InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                SQLException |
                IllegalAccessException |
                ClassNotFoundException e) {
            LOG.error("Error occurred while adding photographer", e);
        }
    }

    public static boolean isValid(Photographer photographer) {
        return ((photographer.getLastName() != null && !photographer.getLastName().isEmpty() && photographer.getBirthdate() != null && photographer.getBirthdate().isBefore(LocalDate.now())));
    }

    public static List<Photographer> getAllPhotographers() {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            return DALFactory.getDAL().getPhotographers(connection);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | SQLException e) {
            LOG.error("Error occured while getting all photographers", e);
        }
        return null;
    }

    public static void removePhotographer(Photographer photographer) {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            DALFactory.getDAL().deletePhotographer(connection, photographer);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | SQLException e) {
            LOG.error("Error occured while deleting photographer", e);
        }
    }

    public static void updatePhotographer(Photographer photographer) {
        try {
            if(isValid(photographer)) {
                Connection connection = DBManager.getInstance().getConnection();
                DALFactory.getDAL().updatePhotographer(connection, photographer);
            }
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | SQLException e) {
            LOG.error("Error occured while updating photographer", e);
        }
    }
}
