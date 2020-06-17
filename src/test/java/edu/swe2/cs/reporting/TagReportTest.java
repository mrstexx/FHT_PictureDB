package edu.swe2.cs.reporting;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.dal.DBManager;
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

import java.util.Map;

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
public class TagReportTest {

    @Mock
    private DBManager dbManager;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Before
    public void saveIptc() {
        for (Picture picture : PictureBL.getInstance().getAllPictures()) {
            PictureBL.getInstance().updateIptc(picture.getIptc(), picture);
        }
    }

    @Test
    public void testGetTagsData() {
        TagReport report = new TagReport(PictureBL.getInstance().getAllPictures());
        Map<String, Integer> tags = report.getTagsData();
        Assert.assertEquals(2, tags.size());
        Assert.assertEquals(2, tags.get("test").intValue());
        Assert.assertEquals(1, tags.get("photo").intValue());
    }

}
