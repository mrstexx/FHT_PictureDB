package edu.swe2.cs.bl;
import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.dal.DALMock;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.dal.DataAccessException;
import edu.swe2.cs.dal.IDAL;
import edu.swe2.cs.model.Photographer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
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
public class TestPhotographerBL {

    @Mock
    private DBManager dbManager;


    @Before
    public void setUp() {
        PowerMockito.mockStatic(DBManager.class);
        PowerMockito.when(DBManager.getInstance()).thenReturn(dbManager);
        PowerMockito.when(dbManager.getConnection()).thenReturn(null);
    }


    @Test
    public void savePhotographer_validPhotographer_savePhotographerSuccess() {
        LocalDate date = LocalDate.now().minusDays(1);
        Photographer photographer = new Photographer("Marius", "Hochwald", date, "some notes");
        PhotographerBL.savePhotographer(photographer);
        boolean contains = false;
        for (Photographer photographer1 : PhotographerBL.getAllPhotographers()){
            String fN = photographer1.getFirstName();
            String lN = photographer1.getLastName();
            LocalDate bD = photographer1.getBirthdate();
            String notes = photographer1.getNotes();
            if (fN != null && fN.equals("Marius") && lN != null && lN.equals("Hochwald") && bD != null && bD.isEqual(date) && notes != null && notes.equals("some notes")) contains = true;
        }
        Assert.assertTrue("Valid Photographer must be persistet", contains);
    }

    @Test
    public void isValid_invalidPhotographerBirthDate_false() {
        Assert.assertFalse("Birth Date not before now must be invalid", PhotographerBL.isValid(new Photographer("Leo", "Gruber", LocalDate.now(), "")));
    }

    @Test
    public void isValid_invalidPhotographerLastName_false() {
        Assert.assertFalse("Empty last name must be invalid", PhotographerBL.isValid(new Photographer("Leo", "", LocalDate.now().minusDays(1), "")));
    }

    @Test
    public void isValid_validPhotographer_true() {
        Assert.assertTrue("Non empty last name and birth date before now must be valid", PhotographerBL.isValid(new Photographer("Leo", "Gruber", LocalDate.now().minusDays(1), "")));
    }



}
