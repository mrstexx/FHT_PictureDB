package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDAL {


    List<Picture> getPictures(Connection connection) throws SQLException;

    List<Photographer> getPhotographers(Connection connection) throws SQLException;

    List<Exif> getExifs(Connection connection) throws SQLException;

    List<Iptc> getIptcs(Connection connection) throws SQLException;

    Picture getPicture(Connection connection, int id) throws SQLException;

    Photographer getPhotographer(Connection connection, int id) throws SQLException;

    Exif getExif(Connection connection, int id) throws SQLException;

    Iptc getIptc(Connection connection, int id) throws SQLException;

    List<String> getFileNames(Connection connection) throws SQLException;

    int addPicture(Connection connection, Picture picture) throws SQLException;

    int addExif(Connection connection, Exif exif, int picture_id) throws SQLException;

    int updateIptc(Connection connection, Iptc iptc, String fileName) throws SQLException;

    int addPhotographer(Connection connection, Photographer photographer) throws SQLException;

    void deletePhotographer(Connection connection, Photographer photographer) throws SQLException;

    void updatePhotographer(Connection connection, Photographer photographer) throws SQLException;

    void assignPicture(Connection connection, Picture picture, Photographer photographer) throws SQLException;
}
