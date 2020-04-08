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
    public List<Picture> getPictures() throws SQLException {
        return null;
    }

    @Override
    public List<Photographer> getPhotographers() throws SQLException {
        return null;
    }

    @Override
    public List<Exif> getExifs() throws SQLException {
        return null;
    }

    @Override
    public List<Iptc> getIptcs() throws SQLException {
        return null;
    }

    @Override
    public Picture getPicture(int id) throws SQLException {
        return null;
    }

    @Override
    public Photographer getPhotographer(int id) throws SQLException {
        return null;
    }

    @Override
    public Exif getExif(int id) throws SQLException {
        return null;
    }

    @Override
    public Iptc getIptc(int id) throws SQLException {
        return null;
    }

    @Override
    public List<String> getFileNames() throws SQLException {
        return null;
    }

    @Override
    public int addPicture(Picture picture) throws SQLException {
        return 0;
    }

    @Override
    public int addExif(Exif exif, int pictureID) throws SQLException {
        return 0;
    }

    @Override
    public int updateIptc(Iptc iptc, String fileName) throws SQLException {
        return 0;
    }

    @Override
    public int addPhotographer(Photographer photographer) throws SQLException {
        return 0;
    }

    @Override
    public void deletePhotographer(Photographer photographer) throws SQLException {

    }

    @Override
    public void updatePhotographer(Photographer photographer) throws SQLException {

    }

    @Override
    public void assignPicture(Picture picture, Photographer photographer) throws SQLException {

    }

    @Override
    public void setConnection(Connection connection) {

    }
}
