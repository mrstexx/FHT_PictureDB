package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDAL {

    /**
     * Get a list of all stored pictures
     *
     * @return List containing all pictures
     * @throws DataAccessException if dal fails to load stored pictures
     */
    List<Picture> getPictures() throws DataAccessException;

    /**
     * Get a list of all stored photographers
     *
     * @return List containing all photographers
     * @throws DataAccessException if dal fails to load stored photographers
     */
    List<Photographer> getPhotographers() throws DataAccessException;

    /**
     * Get a list of all stored exifs
     *
     * @return List containing all exifs
     * @throws DataAccessException if dal fails to load stored exifs
     */
    List<Exif> getExifs() throws DataAccessException;

    /**
     * Get a list of all stored iptcs
     *
     * @return List containing all iptcs
     * @throws DataAccessException if dal fails to load stored iptcs
     */
    List<Iptc> getIptcs() throws DataAccessException;

    /**
     * Get stored picture corresponding to given id
     *
     * @param id Id of picture to be returned
     * @return Picture containing the given id
     * @throws DataAccessException if dal fails to load stored picture
     */
    Picture getPicture(int id) throws DataAccessException;

    /**
     * Get stored photographer corresponding to given id
     *
     * @param id Id of photographer to be returned
     * @return Photographer containing the given id
     * @throws DataAccessException if dal fails to load stored photographer
     */
    Photographer getPhotographer(int id) throws DataAccessException;

    /**
     * Get stored exif corresponding to given id
     *
     * @param id Id of exif to be returned
     * @return Exif containing the given id
     * @throws DataAccessException if dal fails to load stored exif
     */
    Exif getExif(int id) throws DataAccessException;

    /**
     * Get stored iptc corresponding to given id
     *
     * @param id Id of iptc to be returned
     * @return Iptc containing the given id
     * @throws DataAccessException if dal fails to load stored iptc
     */
    Iptc getIptc(int id) throws DataAccessException;

    /**
     * Get list of filenames of stored pictures
     *
     * @return List containing file names of all stored pictures
     * @throws DataAccessException if dal fails to load file names of stored pictures
     */
    List<String> getFileNames() throws DataAccessException;

    /**
     * Store a given picture in the database
     *
     * @param picture Picture to be stored in data storage
     * @return The id of the newly stored pictured
     * @throws DataAccessException if dal fails to store given picture
     */
    int addPicture(Picture picture) throws DataAccessException;

    /**
     * Store a given exif in data storage
     *
     * @param exif Exif to be stored in data storage
     * @param picture_id Id of the picture that should contain given exif information
     * @return The id of the newly stored exif
     * @throws DataAccessException if dal fails to store given exif
     */
    int addExif(Exif exif, int picture_id) throws DataAccessException;

    /**
     * Used for updating iptc information for a given file name corresponding to a picture
     *
     * @param iptc Iptc data to be stored
     * @param fileName Filename of the picture that should contain the given iptc information
     * @throws DataAccessException if dal fails to update given iptc
     */
    int updateIptc(Iptc iptc, String fileName) throws DataAccessException;

    /**
     * Used to store a photographer in data storage
     *
     * @param photographer Photographer to be stored
     * @return Id of the newly stored photographer
     * @throws DataAccessException if dal fails to add photographer
     */
    int addPhotographer(Photographer photographer) throws DataAccessException;

    /**
     * Used to delete a photographer from the data storage
     *
     * @param photographer Photographer to be deleted
     * @throws DataAccessException if dal fails to delete photographer
     */
    void deletePhotographer(Photographer photographer) throws DataAccessException;

    /**
     * Used to update a photographer in the data storage
     *
     * @param photographer Photographer to be updated
     * @throws DataAccessException if dal fails to update photographer
     */
    void updatePhotographer(Photographer photographer) throws DataAccessException;

    /**
     * Used for assigning a photographer to a given picture
     *
     * @param picture Picture which should be assigned a new photographer
     * @param photographer Photographer to be assigned to the picture
     * @throws DataAccessException if dal fails to assign photographer failed to given picture
     */
    void assignPicture(Picture picture, Photographer photographer) throws DataAccessException;

    /**
     * Set connection for this dal instance to use
     *
     * @param connection Connection to be used by this dal instance
     */
    void setConnection(Connection connection);
}
