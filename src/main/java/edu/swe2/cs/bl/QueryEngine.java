package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.IDAL;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO: REFACTOR

public class QueryEngine {

    private final static Logger LOG = LogManager.getLogger(QueryEngine.class.getName());
    private Map<ECache, Cache> caches;
    private IDAL dal;
    private Connection connection;
    private static QueryEngine instance = null;

    public synchronized static QueryEngine getInstance() {
        if (instance == null) {
            instance = new QueryEngine();
        }
        return instance;
    }

    private QueryEngine() {
        try {
            caches = new HashMap<>();
            dal = DALFactory.getDAL();
            connection = DBManager.getInstance().getConnection();
            initializeCaches();
        } catch (SQLException |
                ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException |
                InstantiationException e) {
            LOG.error("Error occurred while initializing QueryEngine.", e);
        }
    }

    private void initializeCaches() throws SQLException {
        setPictureCache();
        setPhotographerCache();
        setExifCache();
        setIPTCCache();
    }

    private void setIPTCCache() throws SQLException {
        caches.put(ECache.IPTCS, new Cache<Integer, Iptc>());
        Cache iptcCache = getCache(ECache.IPTCS);
        List<Iptc> iptcList = dal.getIptcs(connection);
        if (iptcList != null) iptcList.stream().forEach(iptc -> iptcCache.addData(iptc.getId(), iptc));
    }

    private void setExifCache() throws SQLException {
        caches.put(ECache.EXIFS, new Cache<Integer, Exif>());
        Cache exifCache = getCache(ECache.EXIFS);
        List<Exif> exifList = dal.getExifs(connection);
        if (exifList != null) exifList.stream().forEach(exif -> exifCache.addData(exif.getId(), exif));
    }

    private void setPhotographerCache() throws SQLException {
        caches.put(ECache.PHOTOGRAPHERS, new Cache<Integer, Photographer>());
        Cache pictureCache = getCache(ECache.PHOTOGRAPHERS);
        List<Photographer> photographerList = dal.getPhotographers(connection);
        if (photographerList != null)
            photographerList.stream().forEach(photographer -> pictureCache.addData(photographer.getId(), photographer));
    }


    private void setPictureCache() throws SQLException {
        caches.put(ECache.PICTURES, new Cache<Integer, Picture>());
        Cache pictureCache = getCache(ECache.PICTURES);
        List<Picture> pictureList = dal.getPictures(connection);
        if (pictureList != null)
            pictureList.stream().forEach(picture -> pictureCache.addData(picture.getId(), picture));
    }

    public Cache getCache(ECache eCache) {
        if (caches != null && caches.containsKey(eCache)) {
            return caches.get(eCache);
        }
        return null;
    }

    public void savePhotographer(Photographer photographer) throws SQLException {
        int id = dal.addPhotographer(connection, photographer);
        photographer.setId(id);
        getCache(ECache.PHOTOGRAPHERS).addData(photographer.getId(), photographer);
    }

    public List<Photographer> getAllPhotographers() {
        return getCache(ECache.PHOTOGRAPHERS).getALLData();
    }

    public void removePhotographer(Photographer photographer) throws SQLException {
        dal.deletePhotographer(connection, photographer);
        Cache pictureCache = getCache(ECache.PICTURES);
        List<Picture> pictureList = pictureCache.getALLData();
        if (pictureList != null) pictureList.stream().forEach(picture ->
        {
            if (picture.getPhotographer_id() == photographer.getId()) {
                picture.setPhotographer_id(-1);
                pictureCache.updateData(picture.getId(), picture);
            }
        });
        getCache(ECache.PHOTOGRAPHERS).deleteData(photographer.getId());
    }

    public void updatePhotographer(Photographer photographer) throws SQLException {
        dal.updatePhotographer(connection, photographer);
        getCache(ECache.PHOTOGRAPHERS).updateData(photographer.getId(), photographer);
    }

    public void savePictureWithExif(Picture picture, Exif exif) throws SQLException {
        int id = dal.addPicture(connection, picture);
        picture.setId(id);
        getCache(ECache.PICTURES).addData(picture.getId(), picture);
        addExif(exif, picture.getId());
    }

    public void updateIPTC(Iptc iptc, Picture picture) throws SQLException {
        int id = dal.updateIptc(connection, iptc, picture.getFileName());
        Cache iptcCache = getCache(ECache.IPTCS);
        if (id == -1) {
            iptcCache.updateData(iptc.getId(), iptc);
        } else {
            iptc.setID(id);
            iptcCache.addData(iptc.getId(), iptc);
            picture.setIptc_id(id);
            getCache(ECache.PICTURES).updateData(picture.getId(), picture);
        }
    }

    public List<Picture> getAllPictures() {
        return getCache(ECache.PICTURES).getALLData();
    }

    public void assignPicture(Picture picture, Photographer oldPhotographer, Photographer photographer) throws SQLException {
        dal.assignPicture(connection, picture, photographer);
        picture.setPhotographer_id(photographer.getId());
        getCache(ECache.PICTURES).updateData(picture.getId(), picture);
    }

    public void addExif(Exif exif, int pictureID) throws SQLException {
        int id = dal.addExif(connection, exif, pictureID);
        exif.setId(id);
        getCache(ECache.EXIFS).addData(exif.getId(), exif);
        Cache pictureCache = getCache(ECache.PICTURES);
        Picture picture = (Picture) pictureCache.getData(pictureID);
        picture.setExif_id(exif.getId());
        pictureCache.updateData(pictureID, picture);
    }

    public Photographer getPhotographerToPicture(Picture picture) {
        // picture in listviewmodel could have changed since starting the application --> get cached picture
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Photographer) getCache(ECache.PHOTOGRAPHERS).getData(cachedPicture.getPhotographer_id());
    }

    public Exif getExifToPicture(Picture picture) {
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Exif) getCache(ECache.EXIFS).getData(cachedPicture.getExif_id());
    }

    public Iptc getIptcToPicture(Picture picture) {
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Iptc) getCache(ECache.IPTCS).getData(cachedPicture.getIptc_id());
    }

    public Picture getCachedPicture(Picture picture) {
        return (Picture) getCache(ECache.PICTURES).getData(picture.getId());
    }
}
