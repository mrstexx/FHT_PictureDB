package edu.swe2.cs.DAL;

import edu.swe2.cs.Model.Photographer;
import edu.swe2.cs.Model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DALPostgres implements IDAL {

    private final static Logger LOGGER = LogManager.getLogger(DALPostgres.class.getName());

    @Override
    public void initialize() {
        LOGGER.info("Initializing DALPostgres");
    }

    @Override
    public List<Picture> getPictures() {
        return null;
    }

    @Override
    public List<Photographer> getPhotographers() {
        return null;
    }

    @Override
    public Picture getPicture() {
        return null;
    }

    @Override
    public Photographer getPhotographer() {
        return null;
    }
}
