package edu.swe2.cs.dal;

import edu.swe2.cs.bl.FileCache;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DALPostgres implements IDAL {

    private final static Logger LOGGER = LogManager.getLogger(DALPostgres.class.getName());
    private Connection connection;

    public void setConnection(Connection connection) {
        if (this.connection == null) {
            this.connection = connection;
        }
    }

    @Override
    public List<Picture> getPictures() throws SQLException {
        List<Picture> pictures = new ArrayList<>();
        String prepStatement = "SELECT id FROM picture ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            pictures.add(getPicture(rs.getInt("id")));
        }
        return pictures;
    }

    @Override
    public List<Photographer> getPhotographers() throws SQLException {
        List<Photographer> photographers = new ArrayList<>();
        String prepStatement = "SELECT id FROM photographer ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            photographers.add(getPhotographer(id));
        }
        return photographers;
    }

    @Override
    public List<Exif> getExifs() throws SQLException {
        List<Exif> exifList = new ArrayList<>();
        String prepStatement = "SELECT id FROM exif_info ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            exifList.add(getExif(id));
        }
        return exifList;
    }

    @Override
    public List<Iptc> getIptcs() throws SQLException {
        List<Iptc> iptcList = new ArrayList<>();
        String prepStatement = "SELECT id FROM iptc_info ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            iptcList.add(getIptc(id));
        }
        return iptcList;
    }

    @Override
    public Picture getPicture(int id) throws SQLException {
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
    }

    @Override
    public Photographer getPhotographer(int id) throws SQLException {
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
    }

    @Override
    public Exif getExif(int id) throws SQLException {
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
    }

    @Override
    public Iptc getIptc(int id) throws SQLException {
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
        rs.close();
        return iptc;
    }

    @Override
    public List<String> getFileNames() throws SQLException {
        List<String> fileNames = new LinkedList<>();
        String prepStatement = "SELECT file_name FROM picture";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            fileNames.add(rs.getString("file_name"));
        }
        return fileNames;
    }

    @Override
    public int addPicture(Picture picture) throws SQLException {
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
    }

    @Override
    public int addExif(Exif exif, int pictureID) throws SQLException {
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
    }

    @Override
    public int updateIptc(Iptc iptc, String fileName) throws SQLException {
        String prepStatement;
        if (iptc.getId() > -1) {
            prepStatement = "UPDATE iptc_info SET title=?, caption=?, city=? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setString(1, iptc.getTitle());
            preparedStatement.setString(2, iptc.getCaption());
            preparedStatement.setString(3, iptc.getCity());
            preparedStatement.setInt(4, iptc.getId());
            preparedStatement.executeUpdate();
            return -1;
        } else {
            prepStatement = "INSERT INTO iptc_info(title, caption, city) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
            preparedStatement.setString(1, iptc.getTitle());
            preparedStatement.setString(2, iptc.getCaption());
            preparedStatement.setString(3, iptc.getCity());
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
    }

    @Override
    public int addPhotographer(Photographer photographer) throws SQLException {
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
    }

    @Override
    public void deletePhotographer(Photographer photographer) throws SQLException {
        String preStatement = "UPDATE picture SET photographer_id = ? WHERE photographer_id = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(preStatement);
        preparedStatement1.setNull(1, Types.INTEGER);
        preparedStatement1.setInt(2, photographer.getId());
        preparedStatement1.executeUpdate();

        String prepStatement = "DELETE FROM photographer WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        preparedStatement.setInt(1, photographer.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void updatePhotographer(Photographer photographer) throws SQLException {
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
    }

    @Override
    public void assignPicture(Picture picture, Photographer photographer) throws SQLException {
        //TODO: Save
        String preStatement = "UPDATE picture SET photographer_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(preStatement);
        preparedStatement.setInt(1, photographer.getId());
        preparedStatement.setInt(2, picture.getId());
        preparedStatement.executeUpdate();
    }


}
