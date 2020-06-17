package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.dal.IDAL;
import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


//TODO: REFACTOR

public class QueryEngine {

    private final static Logger LOG = LogManager.getLogger(QueryEngine.class.getName());
    private Map<ECache, Cache> caches;
    private IDAL dal;
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
            assert dal != null;
            dal.setConnection(DBManager.getInstance().getConnection());
            initializeCaches();
        } catch (Exception e) {
            LOG.error("Error occurred while initializing QueryEngine.", e);
        }
    }

    private void initializeCaches() throws DataAccessException {
        setPictureCache();
        setPhotographerCache();
        setExifCache();
        setIPTCCache();
    }

    private void setIPTCCache() throws DataAccessException {
        caches.put(ECache.IPTCS, new Cache<Integer, Iptc>());
        Cache iptcCache = getCache(ECache.IPTCS);
        List<Iptc> iptcList = dal.getIptcs();
        if (iptcList != null) {
            iptcList.stream().forEach(iptc -> iptcCache.addData(iptc.getId(), iptc));
        }
    }

    private void setExifCache() throws DataAccessException {
        caches.put(ECache.EXIFS, new Cache<Integer, Exif>());
        Cache exifCache = getCache(ECache.EXIFS);
        List<Exif> exifList = dal.getExifs();
        if (exifList != null) {
            exifList.stream().forEach(exif -> exifCache.addData(exif.getId(), exif));
        }
    }

    private void setPhotographerCache() throws DataAccessException {
        caches.put(ECache.PHOTOGRAPHERS, new Cache<Integer, Photographer>());
        Cache pictureCache = getCache(ECache.PHOTOGRAPHERS);
        List<Photographer> photographerList = dal.getPhotographers();
        if (photographerList != null) {
            photographerList.stream().forEach(photographer -> pictureCache.addData(photographer.getId(), photographer));
        }
    }


    private void setPictureCache() throws DataAccessException {
        caches.put(ECache.PICTURES, new Cache<Integer, Picture>());
        Cache pictureCache = getCache(ECache.PICTURES);
        List<Picture> pictureList = dal.getPictures();
        if (pictureList != null) {
            pictureList.stream().forEach(picture -> pictureCache.addData(picture.getId(), picture));
        }
    }

    public Cache getCache(ECache eCache) {
        if (caches != null && caches.containsKey(eCache)) {
            return caches.get(eCache);
        }
        return null;
    }

    public void savePhotographer(Photographer photographer) throws DataAccessException {
        int id = dal.addPhotographer(photographer);
        photographer.setId(id);
        getCache(ECache.PHOTOGRAPHERS).addData(photographer.getId(), photographer);
    }

    public List<Photographer> getAllPhotographers() {
        return getCache(ECache.PHOTOGRAPHERS).getALLData();
    }

    public void removePhotographer(Photographer photographer) throws DataAccessException {
        dal.deletePhotographer(photographer);
        Cache pictureCache = getCache(ECache.PICTURES);
        List<Picture> pictureList = pictureCache.getALLData();
        if (pictureList != null) {
            pictureList.stream().forEach(picture -> {
                if (picture.getPhotographer_id() == photographer.getId()) {
                    picture.setPhotographer_id(-1);
                    pictureCache.updateData(picture.getId(), picture);
                }
            });
        }
        getCache(ECache.PHOTOGRAPHERS).deleteData(photographer.getId());
    }

    public void updatePhotographer(Photographer photographer) throws DataAccessException {
        dal.updatePhotographer(photographer);
        getCache(ECache.PHOTOGRAPHERS).updateData(photographer.getId(), photographer);
    }

    public void savePictureWithExif(Picture picture, Exif exif) throws DataAccessException {
        int id = dal.addPicture(picture);
        picture.setId(id);
        getCache(ECache.PICTURES).addData(picture.getId(), picture);
        addExif(exif, picture.getId());
    }

    public void updateIPTC(Iptc iptc, Picture picture) throws DataAccessException {
        int id = dal.updateIptc(iptc, picture.getFileName());
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

    public void assignPicture(Picture picture, Photographer oldPhotographer, Photographer photographer) throws DataAccessException {
        dal.assignPicture(picture, photographer);
        picture.setPhotographer_id(photographer.getId());
        getCache(ECache.PICTURES).updateData(picture.getId(), picture);
    }

    public void addExif(Exif exif, int pictureID) throws DataAccessException {
        int id = dal.addExif(exif, pictureID);
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

    public List<Picture> getPicturesToSearch(String searchText) {
        List<Picture> pictures = new ArrayList<>();
        List<Picture> allPictures = getCache(ECache.PICTURES).getALLData();
        if (allPictures != null) {
            allPictures.stream().forEach(picture -> {
                String[] keywords = searchText.split(" ");
                for (String key : keywords) {
                    boolean containsSearchText = false;
                    Photographer photographer = (Photographer) getCache(ECache.PHOTOGRAPHERS).getData(picture.getPhotographer_id());
                    if (photographer != null) {
                        containsSearchText = queryPhotographer(photographer, key);
                    }
                    if (!containsSearchText) {
                        Exif exif = (Exif) getCache(ECache.EXIFS).getData(picture.getExif_id());
                        if (exif != null) {
                            containsSearchText = queryExif(exif, key);
                        }
                        if (!containsSearchText) {
                            Iptc iptc = (Iptc) getCache(ECache.IPTCS).getData(picture.getIptc_id());
                            if (iptc != null) {
                                containsSearchText = queryIptc(iptc, key);
                            }
                        }
                    }
                    if (containsSearchText) {
                        pictures.add(picture);
                        break;
                    }
                }
            });
        }
        return pictures;
    }

    private boolean queryPhotographer(Photographer photographer, String searchText) {
        boolean matchFN = false;
        boolean matchLN = false;
        if (photographer != null) {
            String firstName = photographer.getFirstName();
            String lastName = photographer.getLastName();
            if (firstName != null) {
                matchFN = Arrays.stream((firstName).split(" ")).anyMatch(searchText::equals);
            }
            if (lastName != null) {
                matchLN = Arrays.stream((lastName).split(" ")).anyMatch(searchText::equals);
            }
        }
        return matchFN || matchLN;
    }

    private boolean queryExif(Exif exif, String searchText) {
        boolean matchC = false;
        boolean matchL = false;
        if (exif != null) {
            String camera = exif.getCamera();
            String lens = exif.getLens();
            if (camera != null) {
                matchC = Arrays.stream((camera).split(" ")).anyMatch(searchText::equals);
            }
            if (lens != null) {
                matchL = Arrays.stream((lens).split(" ")).anyMatch(searchText::equals);
            }
        }
        return matchC || matchL;
    }

    private boolean queryIptc(Iptc iptc, String searchText) {
        boolean matchC = false;
        boolean matchT = false;
        boolean matchCA = false;
        boolean matchTags = false;
        if (iptc != null) {
            String city = iptc.getCity();
            String title = iptc.getTitle();
            String caption = iptc.getCaption();
            String tags = iptc.getTags();
            if (city != null) {
                matchC = Arrays.stream((city).split(" ")).anyMatch(searchText::equals);
            }
            if (title != null) {
                matchT = Arrays.stream((title).split(" ")).anyMatch(searchText::equals);
            }
            if (caption != null) {
                matchCA = Arrays.stream((caption).split(" ")).anyMatch(searchText::equals);
            }
            if (tags != null) {
                matchTags = Arrays.stream((tags).split(",")).anyMatch(searchText::equals);
            }
        }
        return matchC || matchT || matchCA || matchTags;
    }

}
