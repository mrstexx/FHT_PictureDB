package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBManager.class})
@PowerMockIgnore({
        "javax.management.*",
        "com.sun.org.apache.xerces.*",
        "javax.xml.*", "org.xml.*",
        "org.w3c.dom.*",
        "com.sun.org.apache.xalan.*",
        "javax.activation.*"
})
public class PictureViewModelTest {

    @Mock
    private DBManager dbManager;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Test
    public void testGetCurrentPictureOnSelect() {
        Picture picture = PictureBL.getInstance().getAllPictures().get(0);
        PictureViewModel pictureViewModel = new PictureViewModel();
        // instead of handle
        pictureViewModel.setPicture(picture);
        Assert.assertEquals(PictureBL.getInstance().getCurrentPicture().getId(), pictureViewModel.getPicture().getId());
    }

    @Test
    public void testGetPicturePath() {
        Picture picture = PictureBL.getInstance().getAllPictures().get(1);
        PictureViewModel pictureViewModel = new PictureViewModel();
        // instead of handle
        pictureViewModel.setPicture(picture);
        Assert.assertTrue(pictureViewModel.getPicturePath().contains("file:"));
    }

}
