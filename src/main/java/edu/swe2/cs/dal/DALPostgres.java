package edu.swe2.cs.dal;

import edu.swe2.cs.bl.FileCache;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class DALPostgres implements IDAL {

    private final static Logger LOGGER = LogManager.getLogger(DALPostgres.class.getName());
    private Connection connection;

    /**
     * Set connection for this dal instance to use
     *
     * @param connection Connection to be used by this dal instance
     */
    public void setConnection(Connection connection) {
        if (this.connection == null) {
            this.connection = connection;
        }
    }

    /**
     * Get a list of all stored pictures
     *
     * @return List containing all pictures
     * @throws DataAccessException if dal fails to load stored pictures
     */
    @Override
    public List<Picture> getPictures() throws DataAccessException {
        try {
            List<Picture> pictures = new ArrayList<>();
            String prepStatement = "SELECT id FROM picture ORDER BY ID ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                pictures.add(getPicture(rs.getInt("id")));
            }
            return pictures;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Pictures", e);
        }
    }

    /**
     * Get a list of all stored photographers
     *
     * @return List containing all photographers
     * @throws DataAccessException if dal fails to load stored photographers
     */
    @Override
    public List<Photographer> getPhotographers() throws DataAccessException {
        try {
            List<Photographer> photographers = new ArrayList<>();
            String prepStatement = "SELECT id FROM photographer ORDER BY ID ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                photographers.add(getPhotographer(id));
            }
            return photographers;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Photographers", e);
        }
    }

    /**
     * Get a list of all stored exifs
     *
     * @return List containing all exifs
     * @throws DataAccessException if dal fails to load stored exifs
     */
    @Override
    public List<Exif> getExifs() throws DataAccessException {
        try {
            List<Exif> exifList = new ArrayList<>();
            String prepStatement = "SELECT id FROM exif_info ORDER BY ID ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                exifList.add(getExif(id));
            }
            return exifList;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Exif data", e);
        }
    }

    /**
     * Get a list of all stored iptcs
     *
     * @return List containing all iptcs
     * @throws DataAccessException if dal fails to load stored iptcs
     */
    @Override
    public List<Iptc> getIptcs() throws DataAccessException {
        try {
            List<Iptc> iptcList = new ArrayList<>();
            String prepStatement = "SELECT id FROM iptc_info ORDER BY ID ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                iptcList.add(getIptc(id));
            }
            return iptcList;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load IPTCs", e);
        }
    }

    /**
     * Get stored picture corresponding to given id
     *
     * @param id Id of picture to be returned
     * @return Picture containing the given id
     * @throws DataAccessException if dal fails to load stored picture
     */
    @Override
    public Picture getPicture(int id) throws DataAccessException {
        try {
            boolean hasPhotographer = true;
            boolean hasIptc = true;
            String prepStatement = "SELECT * FROM picture WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            // check if data exists
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();
            }
            int picId = rs.getInt("id");
            int photographerId = rs.getInt("photographer_id");
            if (rs.wasNull()) {
                hasPhotographer = false;
            }
            int exifId = rs.getInt("exif_id");
            int ipctId = rs.getInt("iptc_id");
            if (rs.wasNull()) {
                hasIptc = false;
            }
            String fileName = rs.getString("file_name");
            rs.close();
            Picture picture = new Picture();
            picture.setId(picId);
            picture.setFileName(fileName);
            picture.setExif_id(exifId);
            if (hasIptc) {
                picture.setIptc_id(ipctId);
            }
            if (hasPhotographer) {
                picture.setPhotographer_id(photographerId);
            }
            return picture;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Picture", e);
        }
    }

    /**
     * Get stored photographer corresponding to given id
     *
     * @param id Id of photographer to be returned
     * @return Photographer containing the given id
     * @throws DataAccessException if dal fails to load stored photographer
     */
    @Override
    public Photographer getPhotographer(int id) throws DataAccessException {
        try {
            String prepStatement = "SELECT * FROM photographer WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();
            }
            String lastName = rs.getString("lastname");
            Photographer photographer = new Photographer(id, lastName);
            String firstName = rs.getString("firstname");
            if (!rs.wasNull()) {
                photographer.setFirstName(firstName);
            }
            LocalDate birthdate = rs.getObject("birthdate", LocalDate.class);
            if (!rs.wasNull()) {
                photographer.setBirthdate(birthdate);
            }
            String notes = rs.getString("notes");
            if (!rs.wasNull()) {
                photographer.setNotes(notes);
            }
            rs.close();
            return photographer;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Photographer data", e);
        }
    }

    /**
     * Get stored exif corresponding to given id
     *
     * @param id Id of exif to be returned
     * @return Exif containing the given id
     * @throws DataAccessException if dal fails to load stored exif
     */
    @Override
    public Exif getExif(int id) throws DataAccessException {
        try {
            String prepStatement = "SELECT * FROM exif_info WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();
            }
            String camera = rs.getString("camera");
            String lens = rs.getString("lens");
            Date date = rs.getTimestamp("captureDate");
            rs.close();
            return new Exif(id, camera, lens, date);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Exif", e);
        }
    }

    /**
     * Get stored iptc corresponding to given id
     *
     * @param id Id of iptc to be returned
     * @return Iptc containing the given id
     * @throws DataAccessException if dal fails to load stored iptc
     */
    @Override
    public Iptc getIptc(int id) throws DataAccessException {
        try {
            String prepStatement = "SELECT * FROM iptc_info WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                rs.next();
            }
            Iptc iptc = new Iptc(id);
            String title = rs.getString("title");
            if (!rs.wasNull()) {
                iptc.setTitle(title);
            }
            String caption = rs.getString("caption");
            if (!rs.wasNull()) {
                iptc.setCaption(caption);
            }
            String city = rs.getString("city");
            if (!rs.wasNull()) {
                iptc.setCity(city);
            }
            String tags = rs.getString("tags");
            if (!rs.wasNull()) {
                iptc.addTags(tags);
            }
            rs.close();
            return iptc;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load IPTC data", e);
        }
    }

    /**
     * Get list of filenames of stored pictures
     *
     * @return List containing file names of all stored pictures
     * @throws DataAccessException if dal fails to load file names of stored pictures
     */
    @Override
    public List<String> getFileNames() throws DataAccessException {
        try {
            List<String> fileNames = new LinkedList<>();
            String prepStatement = "SELECT file_name FROM picture";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                fileNames.add(rs.getString("file_name"));
            }
            return fileNames;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to load Picture File Names", e);
        }
    }

    /**
     * Store a given picture in the database
     *
     * @param picture Picture to be stored in the database
     * @return The id of the newly stored pictured
     * @throws DataAccessException if dal fails to store given picture
     */
    @Override
    public int addPicture(Picture picture) throws DataAccessException {
        try {
            String prepStatement = "INSERT INTO picture(photographer_id, file_name) VALUES (?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            if (picture.getPhotographer() != null) {
                preparedStatement.setInt(1, picture.getPhotographer().getId());
            } else {
                preparedStatement.setNull(1, Types.INTEGER);
            }
            String fileName = picture.getFileName();
            preparedStatement.setString(2, fileName);
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();
            rs.next();
            int pictureID = rs.getInt(1);

            // add file to cache after adding to db
            FileCache.getInstance().addFile(fileName);
            return pictureID;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to add Picture", e);
        }
    }

    /**
     * Store a given exif in the database
     *
     * @param exif      Exif to be stored in the database
     * @param pictureID Id of the picture that should contain given exif information
     * @return The id of the newly stored exif
     * @throws DataAccessException if dal fails to store given exif
     */
    @Override
    public int addExif(Exif exif, int pictureID) throws DataAccessException {
        try {
            String prepStatement = "INSERT INTO exif_info(camera, lens, captureDate) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setString(1, exif.getCamera());
            preparedStatement.setString(2, exif.getLens());
            preparedStatement.setTimestamp(3, (Timestamp) exif.getCaptureDate());
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int exifId = rs.getInt(1);
            String preStatement = "UPDATE picture SET exif_id = ? WHERE id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(preStatement);
            preparedStatement1.setInt(1, exifId);
            preparedStatement1.setInt(2, pictureID);
            preparedStatement1.executeUpdate();
            return exifId;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to add Exif", e);
        }
    }

    /**
     * Used for updating iptc information for a given file name corresponding to a picture
     *
     * @param iptc     Iptc data to be stored
     * @param fileName Filename of the picture that should contain the given iptc information
     * @throws DataAccessException if dal fails to update given iptc
     */
    @Override
    public int updateIptc(Iptc iptc, String fileName) throws DataAccessException {
        try {
            String prepStatement;
            if (iptc.getId() > -1) {
                prepStatement = "UPDATE iptc_info SET title=?, caption=?, city=?, tags=? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
                preparedStatement.setString(1, iptc.getTitle());
                preparedStatement.setString(2, iptc.getCaption());
                preparedStatement.setString(3, iptc.getCity());
                preparedStatement.setString(4, iptc.getTags());
                preparedStatement.setInt(5, iptc.getId());
                preparedStatement.executeUpdate();
                return -1;
            } else {
                prepStatement = "INSERT INTO iptc_info(title, caption, city, tags) VALUES (?, ?, ?, ?) RETURNING id";
                PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
                preparedStatement.setString(1, iptc.getTitle());
                preparedStatement.setString(2, iptc.getCaption());
                preparedStatement.setString(3, iptc.getCity());
                preparedStatement.setString(4, iptc.getTags());
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                int iptcID = rs.getInt(1);
                prepStatement = "UPDATE picture SET iptc_id = ? WHERE file_name = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(prepStatement);
                preparedStatement1.setInt(1, iptcID);
                preparedStatement1.setString(2, fileName);
                preparedStatement1.executeUpdate();
                iptc.setID(iptcID);
                return iptcID;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update IPTC informations", e);
        }
    }

    /**
     * Used to store a photographer in the database
     *
     * @param photographer Photographer to be stored
     * @return Id of the newly stored photographer
     * @throws DataAccessException if dal fails to add photographer
     */
    @Override
    public int addPhotographer(Photographer photographer) throws DataAccessException {
        try {
            String prepStatement = "INSERT INTO photographer(lastname, firstname, birthdate, notes) VALUES (?, ?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setString(1, photographer.getLastName());
            preparedStatement.setString(2, photographer.getFirstName());
            if (photographer.getBirthdate() != null) {
                preparedStatement.setObject(3, photographer.getBirthdate());
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }
            preparedStatement.setString(4, photographer.getNotes());
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to add Photographer", e);
        }
    }

    /**
     * Used to delete a photographer from the database
     *
     * @param photographer Photographer to be deleted
     * @throws DataAccessException if dal fails to delete photographer
     */
    @Override
    public void deletePhotographer(Photographer photographer) throws DataAccessException {
        try {
            String preStatement = "UPDATE picture SET photographer_id = ? WHERE photographer_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(preStatement);
            preparedStatement1.setNull(1, Types.INTEGER);
            preparedStatement1.setInt(2, photographer.getId());
            preparedStatement1.executeUpdate();

            String prepStatement = "DELETE FROM photographer WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setInt(1, photographer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to delete Photographer", e);
        }
    }

    /**
     * Used to update a photographer in the database
     *
     * @param photographer Photographer to be updated
     * @throws DataAccessException if dal fails to update photographer
     */
    @Override
    public void updatePhotographer(Photographer photographer) throws DataAccessException {
        try {
            String preStatement = "UPDATE photographer SET lastname = ?, firstname = ?, birthdate = ?, notes = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(preStatement);
            preparedStatement.setString(1, photographer.getLastName());
            preparedStatement.setString(2, photographer.getFirstName());
            if (photographer.getBirthdate() != null) {
                preparedStatement.setObject(3, photographer.getBirthdate());
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }
            preparedStatement.setString(4, photographer.getNotes());
            preparedStatement.setInt(5, photographer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update Photographer", e);
        }
    }

    /**
     * Used for assigning a photographer to a given picture
     *
     * @param picture      Picture which should be assigned a new photographer
     * @param photographer Photographer to be assigned to the picture
     * @throws DataAccessException if dal fails to assign photographer failed to given picture
     */
    @Override
    public void assignPicture(Picture picture, Photographer photographer) throws DataAccessException {
        //TODO: Save
        try {
            String preStatement = "UPDATE picture SET photographer_id = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(preStatement);
            preparedStatement.setInt(1, photographer.getId());
            preparedStatement.setInt(2, picture.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to assign Photographer to Picture", e);
        }
    }
}
