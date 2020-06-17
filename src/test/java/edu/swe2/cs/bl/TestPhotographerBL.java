package edu.swe2.cs.bl;
import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.model.Photographer;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class TestPhotographerBL {

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
