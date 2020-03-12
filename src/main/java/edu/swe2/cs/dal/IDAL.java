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

    Picture getPicture(Connection connection, int id) throws SQLException;

    Photographer getPhotographer(Connection connection, int id) throws SQLException;

    Exif getExif(Connection connection, int id) throws SQLException;

    Iptc getIptc(Connection connection, int id) throws SQLException;

    List<String> getFileNames(Connection connection) throws SQLException;

    void addPicture(Connection connection, Picture picture) throws SQLException;

    void addExif(Connection connection, Exif exif, String fileName) throws SQLException;

}
