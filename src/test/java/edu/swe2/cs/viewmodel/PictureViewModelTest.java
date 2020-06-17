package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.scene.image.Image;
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
@PrepareForTest({DBManager.class, Image.class})
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
        Image image = Mockito.mock(Image.class);
        PowerMockito.whenNew(Image.class).withAnyArguments().thenReturn(image);
    }

    @Test
    public void testGetCurrentPictureOnStartup() throws Exception {
        MainWindowViewModel mainWindowViewModel = new MainWindowViewModel();
        PictureViewModel viewModel = new PictureViewModel();
        mainWindowViewModel.setFirstPicture();
        Assert.assertEquals(PictureBL.getInstance().getCurrentPicture().getId(), viewModel.getPicture().getId());
        Assert.assertEquals(PictureBL.getInstance().getAllPictures().get(0).getId(), viewModel.getPicture().getId());
    }

    @Test
    public void testGetCurrentPictureOnSelect() {
        Picture picture = PictureBL.getInstance().getAllPictures().get(1);
        PictureViewModel viewModel = new PictureViewModel();
        viewModel.handle(new OnPictureSelectEvent(picture));
        Assert.assertEquals(picture.getId(), viewModel.getPicture().getId());
    }

}
