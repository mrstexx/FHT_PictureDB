package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DBManager.class)
@PowerMockIgnore({
        "javax.management.*",
        "com.sun.org.apache.xerces.*",
        "javax.xml.*", "org.xml.*",
        "org.w3c.dom.*",
        "com.sun.org.apache.xalan.*",
        "javax.activation.*"
})

public class TestQueryEngine {

    @Mock
    private DBManager dbManager;


    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Test
    public void assignPicture_existingPhotographerToExistingPicture_assignSuccess() throws DataAccessException {
        Picture toAssign = PictureBL.getInstance().getAllPictures().get(0);
        Photographer photographerToAssign = PhotographerBL.getAllPhotographers().get(1);
        Photographer oldPhotographer = PhotographerBL.getAllPhotographers().get(0);
        QueryEngine.getInstance().assignPicture(toAssign, oldPhotographer, photographerToAssign);
        Assert.assertTrue("Photographer must be assigned to picture", QueryEngine.getInstance().getPhotographerToPicture(toAssign).equals(photographerToAssign));
    }

    @Test
    public void getPicturesToSearch_validSearchText_filteredPictureList() throws DataAccessException {
        QueryEngine queryEngine = QueryEngine.getInstance();
        Photographer photographer = queryEngine.getAllPhotographers().get(1);
        List<Picture> pictureList = queryEngine.getAllPictures();
        queryEngine.assignPicture(pictureList.get(0), new Photographer(10, "random photographer"), photographer);
        String searchText = queryEngine.getPhotographerToPicture(queryEngine.getAllPictures().get(0)).getLastName();
        Assert.assertTrue(queryEngine.getAllPictures().size() != 1);
        List<Picture> queriedList = queryEngine.getPicturesToSearch(searchText);
        Assert.assertTrue("Valid search text must be found", queriedList.size() == 1 && queryEngine.getPhotographerToPicture(queriedList.get(0)).getLastName().equals(searchText));
    }

}
