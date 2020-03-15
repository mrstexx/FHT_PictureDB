package edu.swe2.cs.bl;

import edu.swe2.cs.config.ConfigProperties;
import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.ExifGenerator;
import edu.swe2.cs.util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PictureBL {
    private static final Logger LOG = LogManager.getLogger(PictureBL.class);

    private static String dirPath;
    private static FileCache fileCache = null;
    private static PictureBL instance = null;

    private PictureBL() {
        initialize();
    }

    public synchronized static PictureBL getInstance() {
        if (instance == null) {
            instance = new PictureBL();
        }
        return instance;
    }

    private void initialize() {
        if (dirPath == null) {
            dirPath = URLBuilder.buildURLString(new String[]{"src", "main", "resources", ConfigProperties.getProperty("folderName")});
            if (fileCache == null) {
                fileCache = FileCache.getInstance();
            }
        }
    }

    // files that are in db but get deleted from directory will stay in db
    public void sync() {
        if (dirPath == null) {
            if (fileCache == null) {
                fileCache = FileCache.getInstance();
            }
        }
        assert dirPath != null;
        File dir = new File(dirPath);
        File[] filesList = dir.listFiles();

        // if file not in filecache add it to db
        assert filesList != null;
        for (File file : filesList) {
            if (!fileCache.containsFile(file.getName())) {
                Picture picture = new Picture();
                picture.setFileName(file.getName());
                picture.setExif(ExifGenerator.getExif());
                savePicture(picture);
            }
        }
    }

    public void savePicture(Picture picture) {
        try {
            if (isValid(picture)) {
                Connection connection = DBManager.getInstance().getConnection();
                Objects.requireNonNull(DALFactory.getDAL()).addPicture(connection, picture);
            }
        } catch (InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                SQLException |
                IllegalAccessException |
                ClassNotFoundException e) {
            LOG.error("Error occurred while saving picture", e);
        }
    }

    public List<Picture> getAllPictures() {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            return Objects.requireNonNull(DALFactory.getDAL()).getPictures(connection);
        } catch (SQLException |
                ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException |
                InstantiationException e) {
            LOG.error("Error occurred while getting all pictures", e);
        }
        return new ArrayList<>();
    }

    // BL: Picture without Exif is not valid
    public boolean isValid(Picture picture) {
        if (picture != null) {
            return picture.getFileName() != null && picture.getExif() != null;
        }
        return false;
    }
}
