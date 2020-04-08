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
    public List<Exif> getExifs(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Iptc> getIptcs(Connection connection) throws SQLException {
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
    public int addPicture(Connection connection, Picture picture) throws SQLException {
        return 0;
    }

    @Override
    public int addExif(Connection connection, Exif exif, int pictureID) throws SQLException {
        return 0;
    }

    @Override
    public int updateIptc(Connection connection, Iptc iptc, String fileName) throws SQLException {
        return 0;
    }

    @Override
    public int addPhotographer(Connection connection, Photographer photographer) throws SQLException {
        return 0;
    }

    @Override
    public void deletePhotographer(Connection connection, Photographer photographer) throws SQLException {

    }

    @Override
    public void updatePhotographer(Connection connection, Photographer photographer) throws SQLException {

    }

    @Override
    public void assignPicture(Connection connection, Picture picture, Photographer photographer) throws SQLException {

    }
}
