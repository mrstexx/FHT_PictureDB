package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.bl.QueryEngine;
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

public class TestAssignPictureViewModel {

    @Mock
    private DBManager dbManager;


    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }

    @Test
    public void isCurrentPhotographer_PhotographerWithDifferentID_false() throws DataAccessException {
        AssignPictureViewModel viewModel = new AssignPictureViewModel();
        Picture toAssign = PictureBL.getInstance().getAllPictures().get(0);
        viewModel.setPicture(toAssign);
        Photographer photographerToAssign = PhotographerBL.getAllPhotographers().get(1);
        Photographer oldPhotographer = PhotographerBL.getAllPhotographers().get(0);
        QueryEngine.getInstance().assignPicture(toAssign, oldPhotographer, photographerToAssign);
        Assert.assertFalse("Another Photographer with a different id than current Photographer can not be current Photographer", viewModel.isCurrentPhotographer(oldPhotographer));
    }

    @Test
    public void isCurrentPhotographer_PhotographerWithSameID_true() throws DataAccessException {
        AssignPictureViewModel viewModel = new AssignPictureViewModel();
        Picture toAssign = PictureBL.getInstance().getAllPictures().get(0);
        viewModel.setPicture(toAssign);
        Photographer photographerToAssign = PhotographerBL.getAllPhotographers().get(0);
        Photographer oldPhotographer = PhotographerBL.getAllPhotographers().get(1);
        QueryEngine.getInstance().assignPicture(toAssign, oldPhotographer, photographerToAssign);
        Assert.assertTrue("Another Photographer with the same id as current Photographer must be current Photographer", viewModel.isCurrentPhotographer(photographerToAssign));
    }

}
