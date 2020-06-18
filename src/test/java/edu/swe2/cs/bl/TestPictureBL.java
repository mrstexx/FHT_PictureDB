package edu.swe2.cs.bl;

import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Exif;
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
public class TestPictureBL {

    @Mock
    private DBManager dbManager;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Test
    public void testSearchForTags() {
        List<Picture> pictures = PictureBL.getInstance().getPicturesToSearch("test");
        Assert.assertEquals(2, pictures.size());

        pictures = PictureBL.getInstance().getPicturesToSearch(",,test,,");
        Assert.assertEquals(2, pictures.size());

        pictures = PictureBL.getInstance().getPicturesToSearch(", a,    test,b,c");
        Assert.assertEquals(2, pictures.size());

        pictures = PictureBL.getInstance().getPicturesToSearch("photo  ,  ");
        Assert.assertEquals(1, pictures.size());
    }

    @Test
    public void testIsValidPicture() {
        Picture newPicture = new Picture(99, null, new Exif(1, "", "", null));
        Assert.assertFalse(PictureBL.getInstance().isValidPicture(newPicture));

        newPicture = new Picture(99, "name", new Exif(1, "", "", null));
        Assert.assertTrue(PictureBL.getInstance().isValidPicture(newPicture));
    }

    @Test
    public void testSync() {
        // read files from main, at the end there should be 6 file, 4 new
        PictureBL.getInstance().sync();
        Assert.assertEquals(6, PictureBL.getInstance().getAllPictures().size());
    }


}
