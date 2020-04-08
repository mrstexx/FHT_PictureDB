package edu.swe2.cs.bl;

import edu.swe2.cs.config.ConfigProperties;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.ExifGenerator;
import edu.swe2.cs.util.URLBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class PictureBL {
    private static final Logger LOG = LogManager.getLogger(PictureBL.class);

    private static String dirPath;
    private static FileCache fileCache = null;
    private static PictureBL instance = null;
    private QueryEngine queryEngine = null;
    private Picture currentPicture;

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
            queryEngine = QueryEngine.getInstance();
        }
    }

    // files that are in db but get deleted from directory will stay in db
    public int sync() {
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
        int sizeBefore = fileCache.getSize();
        for (File file : filesList) {
            if (!fileCache.containsFile(file.getName())) {
                Picture picture = new Picture();
                picture.setFileName(file.getName());
                Exif exif = ExifGenerator.getExif();
                savePictureWithExif(picture, exif);
            }
        }

        // return number of added images
        return (fileCache.getSize() - sizeBefore);
    }

    public void savePictureWithExif(Picture picture, Exif exif) { // WITH EXIF
        try {
            if (isValidPicture(picture)) {
                queryEngine.savePictureWithExif(picture, exif);
                /*Connection connection = DBManager.getInstance().getConnection();
                Objects.requireNonNull(DALFactory.getDAL()).addPicture(connection, picture);*/
            }
        } catch (SQLException e) {
            LOG.error("Error occurred while saving picture", e);
        }
    }

    public void updateIptc(Iptc iptcData, Picture picture) {
        try {
            if (isValidIptc(iptcData)) {
                queryEngine.updateIPTC(iptcData, picture);
                LOG.debug("IPTC data with {}-ID update successfully executed", iptcData.getId());
                LOG.info("IPTC data on picture {} successfully updated", picture.getFileName());
            }
        } catch (SQLException e) {
            LOG.error("Error occurred while saving IPTC data for picture {}, {}", picture.getFileName(), e);
        }
    }

    public List<Picture> getAllPictures() {
        return queryEngine.getAllPictures();
    }

    public boolean isValidIptc(Iptc iptc) {
        return iptc != null;
    }

    public boolean isValidPicture(Picture picture) {
        if (picture != null) {
            return picture.getFileName() != null;
        }
        return false;
    }

    public void assignPicture(Picture picture, Photographer oldPhotographer, Photographer photographer) {
        try {
            queryEngine.assignPicture(picture, oldPhotographer, photographer);
        } catch (SQLException e) {
            LOG.error("Error occurred while saving picture", e);
        }
    }

    public Photographer getPhotographerToPicture(Picture picture) {
        return queryEngine.getPhotographerToPicture(picture);
    }

    public Exif getExifToPicture(Picture picture) {
        return queryEngine.getExifToPicture(picture);
    }

    public Iptc getIptcToPicture(Picture picture) {
        return queryEngine.getIptcToPicture(picture);
    }

    public Picture getPicture(Picture picture) {
        return queryEngine.getCachedPicture(picture);
    }

    public List<Picture> getPicturesToSearch(String searchText) {
        if (searchText != null && !searchText.isEmpty()) return queryEngine.getPicturesToSearch(searchText);
        else return null;
    }

    public Picture getCurrentPicture() {
        return currentPicture;
    }

    public void setCurrentPicture(Picture currentPicture) {
        this.currentPicture = currentPicture;
        this.currentPicture.setIptc(getIptcToPicture(currentPicture));
        this.currentPicture.setPhotographer(getPhotographerToPicture(currentPicture));
        this.currentPicture.setExif(getExifToPicture(currentPicture));
    }
}
