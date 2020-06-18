package edu.swe2.cs.bl;

import edu.swe2.cs.config.ConfigProperties;
import edu.swe2.cs.dal.DataAccessException;
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
import java.util.ArrayList;
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

    /**
     * Get an instance of PictureBL
     *
     * @return Singleton class instance
     */
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

    /**
     * Used for synchronizing the file directory with persistence layer. Files that are newly added to the directory get stored in the data base.
     *
     * @return Number of newly added pictures
     */
    // files that are in db but get deleted from directory will stay in db
    public int sync() {
        LOG.info("Synchronizing the file directory with persistence layer...");
        if (dirPath == null && fileCache == null) {
            fileCache = FileCache.getInstance();
        }
        assert dirPath != null;
        File dir = new File(dirPath);
        File[] filesList = dir.listFiles();

        // if file not in file cache add it to db
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
        LOG.info("Synchronizing DONE!");
        // return number of added images
        return (fileCache.getSize() - sizeBefore);
    }

    /**
     * Used for converting an array of files to a list of Strings representing the file names.
     *
     * @param filesList Arrays of files
     * @return List of file names
     */
    private List<String> getFilesList(File[] filesList) {
        List<String> list = new ArrayList<>();
        for (File file : filesList) {
            list.add(file.getName());
        }
        return list;
    }

    /**
     * Used for storing a picture and related exif information
     *
     * @param picture Picture to be saved
     * @param exif    Exif to be saved
     */
    public void savePictureWithExif(Picture picture, Exif exif) { // WITH EXIF
        try {
            if (isValidPicture(picture)) {
                queryEngine.savePictureWithExif(picture, exif);
            }
        } catch (DataAccessException e) {
            LOG.error("Unable to save picture with EXIF, with filename = '" + picture.getFileName() + "'", e);
        }
    }

    /**
     * Used for updating iptc information for a given picture
     *
     * @param iptcData Iptc data to be stored
     * @param picture  Picture that should contain the given iptc information
     */
    public void updateIptc(Iptc iptcData, Picture picture) {
        try {
            if (isValidIptc(iptcData)) {
                queryEngine.updateIPTC(iptcData, picture);
            }
        } catch (DataAccessException e) {
            LOG.error("Error occurred while saving IPTC data for picture {}, {}", picture.getFileName(), e);
        }
    }

    /**
     * Get a list of all stored Pictures
     *
     * @return List containing all pictures
     */
    public List<Picture> getAllPictures() {
        return queryEngine.getAllPictures();
    }

    /**
     * Used to validate an iptc entity
     *
     * @param iptc Iptc to be validated
     * @return True if given iptc is not null
     */
    public boolean isValidIptc(Iptc iptc) {
        return iptc != null;
    }

    /**
     * Used to validate a picture entity
     *
     * @param picture Picture to be validated
     * @return True if given picture and contained file name are not null
     */
    public boolean isValidPicture(Picture picture) {
        if (picture != null) {
            return picture.getFileName() != null;
        }
        return false;
    }

    /**
     * Used for assigning a photographer to a given picture
     *
     * @param picture         Picture which should be assigned a new photographer
     * @param oldPhotographer Current photographer assigned to the given picture
     * @param photographer    Photographer to be assigned to the picture
     */
    public void assignPicture(Picture picture, Photographer oldPhotographer, Photographer photographer) {
        try {
            queryEngine.assignPicture(picture, oldPhotographer, photographer);
        } catch (DataAccessException e) {
            LOG.error("Unable to assing photographer to picture", e);
        }
    }

    /**
     * Get assigned photographer for a given picture
     *
     * @param picture Picture to which the assigned photographer should be returned
     * @return Photographer that is currently assigned to given picture
     */
    public Photographer getPhotographerToPicture(Picture picture) {
        return queryEngine.getPhotographerToPicture(picture);
    }

    /**
     * Get assigned exif information for a given picture
     *
     * @param picture Picture to which the related exif information should be returned
     * @return Exif that is currently assigned to given picture
     */
    public Exif getExifToPicture(Picture picture) {
        return queryEngine.getExifToPicture(picture);
    }

    /**
     * Get assigned iptc information for a given picture
     *
     * @param picture Picture to which the assigned iptc information should be returned
     * @return Iptc that is currently assigned to given picture
     */
    public Iptc getIptcToPicture(Picture picture) {
        return queryEngine.getIptcToPicture(picture);
    }

    /**
     * Get cached picture to a given picture
     *
     * @param picture Picture to which the cached picture should be returned
     * @return The cached picture
     */
    public Picture getPicture(Picture picture) {
        return queryEngine.getCachedPicture(picture);
    }

    /**
     * Get a list of pictures that match a given search text
     *
     * @param searchText Search text to specify the matching for pictures
     * @return List of pictures that match the given search text
     */
    public List<Picture> getPicturesToSearch(String searchText) {
        if (searchText != null && !searchText.isEmpty()) {
            return queryEngine.getPicturesToSearch(searchText);
        } else {
            return null;
        }
    }

    /**
     * Get the currently by the user selected picture
     *
     * @return The currently by the user selected picture
     */
    public Picture getCurrentPicture() {
        return currentPicture;
    }

    /**
     * Set the currently by the user selected picture
     *
     * @param currentPicture Picture to be set as the currently by the user selected picture
     */
    public void setCurrentPicture(Picture currentPicture) {
        this.currentPicture = currentPicture;
        this.currentPicture.setIptc(getIptcToPicture(currentPicture));
        this.currentPicture.setPhotographer(getPhotographerToPicture(currentPicture));
        this.currentPicture.setExif(getExifToPicture(currentPicture));
    }
}
