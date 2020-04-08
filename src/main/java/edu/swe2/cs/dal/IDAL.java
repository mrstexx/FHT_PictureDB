package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDAL {

    List<Picture> getPictures() throws SQLException;

    List<Photographer> getPhotographers() throws SQLException;

    List<Exif> getExifs() throws SQLException;

    List<Iptc> getIptcs() throws SQLException;

    Picture getPicture(int id) throws SQLException;

    Photographer getPhotographer(int id) throws SQLException;

    Exif getExif(int id) throws SQLException;

    Iptc getIptc(int id) throws SQLException;

    List<String> getFileNames() throws SQLException;

    int addPicture(Picture picture) throws SQLException;

    int addExif(Exif exif, int picture_id) throws SQLException;

    int updateIptc(Iptc iptc, String fileName) throws SQLException;

    int addPhotographer(Photographer photographer) throws SQLException;

    void deletePhotographer(Photographer photographer) throws SQLException;

    void updatePhotographer(Photographer photographer) throws SQLException;

    void assignPicture(Picture picture, Photographer photographer) throws SQLException;

    void setConnection(Connection connection);
}
