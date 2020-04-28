package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDAL {

    List<Picture> getPictures() throws DataAccessException;

    List<Photographer> getPhotographers() throws DataAccessException;

    List<Exif> getExifs() throws DataAccessException;

    List<Iptc> getIptcs() throws DataAccessException;

    Picture getPicture(int id) throws DataAccessException;

    Photographer getPhotographer(int id) throws DataAccessException;

    Exif getExif(int id) throws DataAccessException;

    Iptc getIptc(int id) throws DataAccessException;

    List<String> getFileNames() throws DataAccessException;

    int addPicture(Picture picture) throws DataAccessException;

    int addExif(Exif exif, int picture_id) throws DataAccessException;

    int updateIptc(Iptc iptc, String fileName) throws DataAccessException;

    int addPhotographer(Photographer photographer) throws DataAccessException;

    void deletePhotographer(Photographer photographer) throws DataAccessException;

    void updatePhotographer(Photographer photographer) throws DataAccessException;

    void assignPicture(Picture picture, Photographer photographer) throws DataAccessException;

    void setConnection(Connection connection);
}
