package edu.swe2.cs.dal;

import edu.swe2.cs.model.Exif;
import edu.swe2.cs.model.Iptc;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.ExifGenerator;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DALMock implements IDAL {

    private List<Photographer> photographers;
    private List<Picture> pictures;
    private List<Exif> exifs;
    private List<Iptc> iptcs;
    private AtomicInteger photgraphersId = new AtomicInteger(0);
    private AtomicInteger picturesId = new AtomicInteger(0);
    private AtomicInteger exifsId = new AtomicInteger(0);
    private AtomicInteger iptcsId = new AtomicInteger(0);


    public DALMock() {
        initialize();
    }

    private void initialize() {
        initializePhotographers();
        initializeExifs();
        initializePictures();
        initializeIptcs();
    }

    private void initializePhotographers() {
        photographers = new ArrayList<>();
        photographers.add(new Photographer(photgraphersId.incrementAndGet(), "Hochwald"));
        photographers.add(new Photographer(photgraphersId.incrementAndGet(), "Miljevic"));
        photographers.add(new Photographer(photgraphersId.incrementAndGet(), "Gruber"));
    }

    private void initializeExifs() {
        exifs = new ArrayList<>();
    }

    private void initializePictures() {
        pictures = new ArrayList<>();
        Exif exif = ExifGenerator.getExif();
        exif.setId(exifsId.incrementAndGet());
        exifs.add(exif);
        Exif exif2 = ExifGenerator.getExif();
        exif2.setId(exifsId.incrementAndGet());
        exifs.add(exif2);
        pictures.add(new Picture(picturesId.incrementAndGet(), "img-1.jpg", exif));
        pictures.add(new Picture(picturesId.incrementAndGet(), "img-2.jpg", exif2));
    }

    private void initializeIptcs() {
        iptcs = new ArrayList<>();
        Iptc iptc1 = new Iptc();
        iptc1.addTags("test,photo");
        Iptc iptc2 = new Iptc();
        iptc2.addTags("test");
        pictures.get(0).setIptc(iptc1);
        pictures.get(1).setIptc(iptc2);
    }

    @Override
    public List<Picture> getPictures() throws DataAccessException {
        if (pictures == null) throw new DataAccessException("Access failed");
        return pictures;
    }

    @Override
    public List<Photographer> getPhotographers() throws DataAccessException {
        if (photographers == null) throw new DataAccessException("Access failed");
        return photographers;
    }

    @Override
    public List<Exif> getExifs() throws DataAccessException {
        if (exifs == null) throw new DataAccessException("Access failed");
        return exifs;
    }

    @Override
    public List<Iptc> getIptcs() throws DataAccessException {
        if (iptcs == null) throw new DataAccessException("Access failed");
        return iptcs;
    }

    @Override
    public Picture getPicture(int id) throws DataAccessException {
        if (pictures == null) throw new DataAccessException("Access failed");
        for (Picture pic : getPictures()) {
            if (pic.getId() == id) return pic;
        }
        return null;
    }

    @Override
    public Photographer getPhotographer(int id) throws DataAccessException {
        if (photographers == null) throw new DataAccessException("Access failed");
        for (Photographer photographer : getPhotographers()) {
            if (photographer.getId() == id) return photographer;
        }
        return null;
    }

    @Override
    public Exif getExif(int id) throws DataAccessException {
        if (exifs == null) throw new DataAccessException("Access failed");
        for (Exif exif : getExifs()) {
            if (exif.getId() == id) return exif;
        }
        return null;
    }

    @Override
    public Iptc getIptc(int id) throws DataAccessException {
        if (iptcs == null) throw new DataAccessException("Access failed");
        for (Iptc iptc : getIptcs()) {
            if (iptc.getId() == id) return iptc;
        }
        return null;
    }

    @Override
    public List<String> getFileNames() throws DataAccessException {
        if (pictures == null) throw new DataAccessException("Access failed");
        List<String> fileNames = new ArrayList<>();
        for (Picture picture : getPictures()) fileNames.add(picture.getFileName());
        return fileNames;
    }

    @Override
    public int addPicture(Picture picture) throws DataAccessException {
        if (pictures == null) throw new DataAccessException("Access failed");
        picture.setId(picturesId.incrementAndGet());
        pictures.add(picture);
        return 0;
    }

    @Override
    public int addExif(Exif exif, int pictureID) throws DataAccessException {
        if (exifs == null) throw new DataAccessException("Access failed");
        int id = exifsId.incrementAndGet();
        exif.setId(id);
        exifs.add(exif);
        getPicture(pictureID).setExif_id(id);
        return id;
    }

    @Override
    public int updateIptc(Iptc iptc, String fileName) throws DataAccessException {
        if (iptcs == null) throw new DataAccessException("Access failed");
        if (pictures == null) throw new DataAccessException("Access failed");
        if (iptc.getId() > -1) {
            List<Iptc> iptcList = iptcs;
            for (int idx = 0; idx < iptcList.size(); idx++) {
                if (iptcList.get(idx).getId() == iptc.getId()) iptcs.set(idx, iptc);
            }
            return -1;
        } else {
            int iptcID = iptcsId.incrementAndGet();
            iptc.setID(iptcID);
            int picID = getPictureIDToFileName(fileName);
            List<Picture> pictureList = pictures;
            for (int idx = 0; idx < pictureList.size(); idx++) {
                if (pictureList.get(idx).getId() == picID) {
                    Picture pic = pictureList.get(idx);
                    pic.setIptc_id(iptcID);
                    pictures.set(idx, pic);
                }
            }
            return iptcID;
        }
    }

    private int getPictureIDToFileName(String fileName) {
        for (Picture pic : pictures) {
            if (pic.getFileName().equals(fileName)) return pic.getId();
        }
        return -1;
    }

    @Override
    public int addPhotographer(Photographer photographer) throws DataAccessException {
        int id = photgraphersId.incrementAndGet();
        photographer.setId(id);
        photographers.add(photographer);
        return id;
    }

    @Override
    public void deletePhotographer(Photographer photographer) throws DataAccessException {
        List<Photographer> photographerList = photographers;
        int id = photographer.getId();
        for (int idx = 0; idx < photographerList.size(); idx++) {
            if (photographerList.get(idx).getId() == id) {
                photographers.remove(idx);
                updatePhotographerIdInPic(id, -1);
            }
        }
    }

    private void updatePhotographerIdInPic(int photographerID, int newPhotographerID) {
        List<Picture> pictureList = pictures;
        for (int idxPic = 0; idxPic < pictureList.size(); idxPic++) {
            if (pictureList.get(idxPic).getPhotographer_id() == photographerID) {
                Picture pic = pictureList.get(idxPic);
                pic.setPhotographer_id(newPhotographerID);
                pictures.set(idxPic, pic);
            }
        }
    }

    @Override
    public void updatePhotographer(Photographer photographer) throws DataAccessException {
        List<Photographer> photographerList = photographers;
        for (int idx = 0; idx < photographerList.size(); idx++) {
            if (photographerList.get(idx).getId() == photographer.getId()) photographers.set(idx, photographer);
        }
    }

    @Override
    public void assignPicture(Picture picture, Photographer photographer) throws DataAccessException {
        List<Picture> pictureList = pictures;
        for (int idx = 0; idx < pictureList.size(); idx++) {
            if (pictureList.get(idx).getId() == picture.getId()) {
                Picture pic = pictureList.get(idx);
                pic.setPhotographer_id(photographer.getId());
                pictures.set(idx, pic);
            }
        }
    }

    @Override
    public void setConnection(Connection connection) {

    }
}
