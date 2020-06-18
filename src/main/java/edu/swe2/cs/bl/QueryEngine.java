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

    /**
     * Get an instance of QueryEngine
     *
     * @return Singleton class instance
     */
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
            LOG.error("Error occurred while initializing QueryEngine.");
            LOG.error(e.getLocalizedMessage());
        }
    }

    private void initializeCaches() throws DataAccessException {
        LOG.info("Initializing query engine cache data...");
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

    /**
     * Get managed cache instance for a given Enum ECache
     *
     * @param eCache Enum to identify a specific cache
     * @return Cache instance corresponding to given enum
     */
    public Cache getCache(ECache eCache) {
        if (caches != null && caches.containsKey(eCache)) {
            return caches.get(eCache);
        }
        return null;
    }

    /**
     * Used to store a photographer
     *
     * @param photographer Photographer to be stored
     * @throws DataAccessException if photographer can not be added
     */
    public void savePhotographer(Photographer photographer) throws DataAccessException {
        LOG.info("Saving new photographer with the name '{}'...", photographer.getFirstName());
        int id = dal.addPhotographer(photographer);
        photographer.setId(id);
        getCache(ECache.PHOTOGRAPHERS).addData(photographer.getId(), photographer);
        LOG.info("Saving new photographer DONE!");
    }

    /**
     * Get a list of all stored Photographers
     *
     * @return List containing all photographers
     */
    public List<Photographer> getAllPhotographers() {
        LOG.info("Fetching all photographer data...");
        return getCache(ECache.PHOTOGRAPHERS).getALLData();
    }

    /**
     * Used to remove a photographer entity
     *
     * @param photographer Photographer to be removed
     * @throws DataAccessException if photographer can not be removed
     */
    public void removePhotographer(Photographer photographer) throws DataAccessException {
        LOG.info("Removing photographer with the name '{}'...", photographer.getFirstName());
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
        LOG.info("Removing photographer data DONE!");
    }

    /**
     * Used to update a photographer entity
     *
     * @param photographer Photographer to be updated
     * @throws DataAccessException if photographer can not be updated
     */
    public void updatePhotographer(Photographer photographer) throws DataAccessException {
        LOG.info("Updating photographer data with the name '{}'...", photographer.getFirstName());
        dal.updatePhotographer(photographer);
        getCache(ECache.PHOTOGRAPHERS).updateData(photographer.getId(), photographer);
        LOG.info("Updating photographer data DONE!");
    }

    /**
     * Used for storing a picture and related exif information
     *
     * @param picture Picture to be saved
     * @param exif    Exif to be saved
     * @throws DataAccessException if picture and exif can not be added
     */
    public void savePictureWithExif(Picture picture, Exif exif) throws DataAccessException {
        LOG.info("Saving picture data for '{}' with EXIF...", picture.getFileName());
        int id = dal.addPicture(picture);
        picture.setId(id);
        getCache(ECache.PICTURES).addData(picture.getId(), picture);
        addExif(exif, picture.getId());
        LOG.info("Saving picture data with exif DONE!");
    }

    /**
     * Used for updating iptc information for a given picture
     *
     * @param iptc    Iptc data to be stored
     * @param picture Picture that should contain the given iptc information
     * @throws DataAccessException if iptc can not be updated
     */
    public void updateIPTC(Iptc iptc, Picture picture) throws DataAccessException {
        LOG.info("Updating IPTC data for picture '{}' ...", picture.getFileName());
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
        LOG.info("Updating IPTC data for picture DONE!");
    }

    /**
     * Get a list of all stored Pictures
     *
     * @return List containing all pictures
     */
    public List<Picture> getAllPictures() {
        LOG.info("Fetching all pictures data...");
        return getCache(ECache.PICTURES).getALLData();
    }

    /**
     * Used for assigning a photographer to a given picture
     *
     * @param picture         Picture which should be assigned a new photographer
     * @param oldPhotographer Current photographer assigned to the given picture
     * @param photographer    Photographer to be assigned to the picture
     * @throws DataAccessException if photographer failed to be assigned to given picture
     */
    public void assignPicture(Picture picture, Photographer oldPhotographer, Photographer photographer) throws DataAccessException {
        LOG.info("Assigning photographer '{}' with picture '{}' ...", photographer.getFirstName(), picture.getFileName());
        dal.assignPicture(picture, photographer);
        picture.setPhotographer_id(photographer.getId());
        getCache(ECache.PICTURES).updateData(picture.getId(), picture);
        LOG.info("Assingning photographer to picture DONE!");
    }

    /**
     * Used for adding exif information for a given picture
     *
     * @param exif      Exif data to be stored
     * @param pictureID Picture id corresponding to the picture that should contain the given exif information
     * @throws DataAccessException if exif failed to be stored
     */
    public void addExif(Exif exif, int pictureID) throws DataAccessException {
        int id = dal.addExif(exif, pictureID);
        exif.setId(id);
        getCache(ECache.EXIFS).addData(exif.getId(), exif);
        Cache pictureCache = getCache(ECache.PICTURES);
        Picture picture = (Picture) pictureCache.getData(pictureID);
        picture.setExif_id(exif.getId());
        pictureCache.updateData(pictureID, picture);
    }

    /**
     * Get assigned photographer for a given picture
     *
     * @param picture Picture to which the assigned photographer should be returned
     * @return Photographer that is currently assigned to given picture
     */
    public Photographer getPhotographerToPicture(Picture picture) {
        // picture in listviewmodel could have changed since starting the application --> get cached picture
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Photographer) getCache(ECache.PHOTOGRAPHERS).getData(cachedPicture.getPhotographer_id());
    }

    /**
     * Get assigned exif information for a given picture
     *
     * @param picture Picture to which the related exif information should be returned
     * @return Exif that is currently assigned to given picture
     */
    public Exif getExifToPicture(Picture picture) {
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Exif) getCache(ECache.EXIFS).getData(cachedPicture.getExif_id());
    }

    /**
     * Get assigned iptc information for a given picture
     *
     * @param picture Picture to which the assigned iptc information should be returned
     * @return Iptc that is currently assigned to given picture
     */
    public Iptc getIptcToPicture(Picture picture) {
        Picture cachedPicture = (Picture) getCache(ECache.PICTURES).getData(picture.getId());
        return (Iptc) getCache(ECache.IPTCS).getData(cachedPicture.getIptc_id());
    }

    /**
     * Get cached picture to a given picture
     *
     * @param picture Picture to which the cached picture should be returned
     * @return The cached picture
     */
    public Picture getCachedPicture(Picture picture) {
        return (Picture) getCache(ECache.PICTURES).getData(picture.getId());
    }

    /**
     * Get a list of pictures that match a given search text
     *
     * @param searchText Search text to specify the matching for pictures
     * @return List of pictures that match the given search text
     */
    public List<Picture> getPicturesToSearch(String searchText) {
        LOG.info("Searching for the picture with search text '{}'...", searchText);
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
                matchFN = Arrays.stream((firstName).split(" ")).anyMatch(searchText::equalsIgnoreCase);
            }
            if (lastName != null) {
                matchLN = Arrays.stream((lastName).split(" ")).anyMatch(searchText::equalsIgnoreCase);
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
                matchC = Arrays.stream((camera).split(" ")).anyMatch(searchText::equalsIgnoreCase);
            }
            if (lens != null) {
                matchL = Arrays.stream((lens).split(" ")).anyMatch(searchText::equalsIgnoreCase);
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
                matchC = Arrays.stream((city).split(" ")).anyMatch(searchText::equalsIgnoreCase);
            }
            if (title != null) {
                matchT = Arrays.stream((title).split(" ")).anyMatch(searchText::equalsIgnoreCase);
            }
            if (caption != null) {
                matchCA = Arrays.stream((caption).split(" ")).anyMatch(searchText::equalsIgnoreCase);
            }
            if (tags != null) {
                matchTags = !Collections.disjoint(new HashSet<>(Arrays.asList(tags.split(","))), Arrays.asList(searchText.split(",")));
            }
        }
        return matchC || matchT || matchCA || matchTags;
    }

}
