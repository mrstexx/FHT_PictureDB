package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DALMock implements IDAL {


    @Override
    public List<Picture> getPictures(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Photographer> getPhotographers(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public Picture getPicture(Connection connection, int id) throws SQLException {
        return null;
    }

    @Override
    public Photographer getPhotographer(Connection connection, int id) throws SQLException {
        return null;
    }

    @Override
    public Exif getExif(Connection connection, int id) throws SQLException {
        return null;
    }

    @Override
    public Iptc getIptc(Connection connection, int id) throws SQLException {
        return null;
    }

    @Override
    public List<String> getFileNames(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public void addPicture(Connection connection, Picture picture) throws SQLException {

    }

    @Override
    public void addExif(Connection connection, Exif exif, String fileName) throws SQLException {

    }
}
