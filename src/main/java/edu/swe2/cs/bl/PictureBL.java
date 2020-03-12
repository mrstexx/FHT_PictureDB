package edu.swe2.cs.bl;

import edu.swe2.cs.config.ConfigProperties;
import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.ExifGenerator;
import edu.swe2.cs.util.URLBuilder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class PictureBL {
    private static String dirPath;
    private static FileCache fileCache = null;
    private static PictureBL instance = null;

    private PictureBL() {
        intialize();
    }

    public static PictureBL getInstance() {
        if (instance == null) {
            instance = new PictureBL();
        }
        return instance;
    }

    private void intialize() {
        if (this.dirPath == null) {
            dirPath = URLBuilder.buildURLString(new String[]{"src", "main", "resources", ConfigProperties.getProperty("folderName")});
            if (this.fileCache == null) {
                fileCache = FileCache.getInstance();
            }
        }

    }

    // files that are in db but get deleted from directory will stay in db
    public void sync() {
        if (dirPath == null)
            if (fileCache == null) {
                fileCache = FileCache.getInstance();
            }

        File dir = new File(dirPath);
        File[] filesList = dir.listFiles();

        // if file not in filecache add it to db
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // BL: Picture without Exif is not valid
    public boolean isValid(Picture picture) {
        if (picture != null) {
            if (picture.getFileName() != null && picture.getExif() != null) {
                return true;
            }
        }
        return false;
    }

}
