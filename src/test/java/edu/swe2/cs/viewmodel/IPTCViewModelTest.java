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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
public class IPTCViewModelTest {
    @Mock
    private DBManager dbManager;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Test
    public void testSaveIptcData() {
        IPTCViewModel viewModel = new IPTCViewModel();
        Picture picture = PictureBL.getInstance().getAllPictures().get(0);
        viewModel.handle(new OnPictureSelectEvent(picture));
        viewModel.titleProperty().setValue("Some value");
        viewModel.cityProperty().setValue("Some city");
        viewModel.captionProperty().setValue("Some notes");
        viewModel.tagsProperty().setValue(",,b,,a");
        // save data
        viewModel.saveData();

        Assert.assertEquals("Some notes", picture.getIptc().getCaption());
        Assert.assertEquals("Some city", picture.getIptc().getCity());
        Assert.assertEquals("Some value", picture.getIptc().getTitle());
        Assert.assertEquals(2, picture.getIptc().getAllTags().size());
        Assert.assertTrue(picture.getIptc().getAllTags().contains("b"));
        Assert.assertTrue(picture.getIptc().getAllTags().contains("a"));
    }

    @Test
    public void testSamePictureInExifAndIptcViewOnSelect() {
        Picture picture = PictureBL.getInstance().getAllPictures().get(1);
        IPTCViewModel iptcViewModel = new IPTCViewModel();
        EXIFViewModel exifViewModel = new EXIFViewModel();
        PictureViewModel viewModel = new PictureViewModel();
        viewModel.handle(new OnPictureSelectEvent(picture));

        Assert.assertEquals(picture.getId(), viewModel.getPicture().getId());
        Assert.assertEquals(picture.getExif().getCamera(), exifViewModel.cameraProperty().getValue());
        Assert.assertEquals(picture.getIptc().getTitle(), iptcViewModel.titleProperty().getValue());
    }

}
