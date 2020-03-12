package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DALPostgres implements IDAL {

    private final static Logger LOGGER = LogManager.getLogger(DALPostgres.class.getName());


    @Override
    public List<Picture> getPictures(Connection connection) throws SQLException {
        List<Picture> pictures = new ArrayList<>();
        String prepStatement = "SELECT id FROM picture ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            pictures.add(getPicture(connection, rs.getInt("id")));
        }
        return pictures;
    }

    @Override
    public List<Photographer> getPhotographers(Connection connection) throws SQLException {
        List<Photographer> photographers = new ArrayList<>();
        String prepStatement = "SELECT id FROM photographer ORDER BY ID ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(prepStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            photographers.add(getPhotographer(connection, id));
        }
        return photographers;
    }

    @Override
    public Picture getPicture(Connection connection, int id) throws SQLException {
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
        Exif exif = getExif(connection, exifId);
        Picture picture = new Picture(picId, fileName, exif);
        if (hasIptc) {
            Iptc iptc = getIptc(connection, ipctId);
            if (iptc != null) {
                picture.setIptc(iptc);
            }
        }
        if (hasPhotographer) {
            Photographer photographer = getPhotographer(connection, photographerId);
            if (photographer != null) {
                picture.setPhotographer(photographer);
            }
        }
        return picture;
    }

    @Override
    public Photographer getPhotographer(Connection connection, int id) throws SQLException {
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
        Date birthdate = rs.getDate("birthdate");
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
    public Exif getExif(Connection connection, int id) throws SQLException {
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
        Date date = rs.getTimestamp("date");
        rs.close();
        return new Exif(id, camera, lens, date);
    }

    @Override
    public Iptc getIptc(Connection connection, int id) throws SQLException {
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

}
