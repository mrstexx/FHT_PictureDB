package edu.swe2.cs;

import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.viewmodel.PhotographerViewModel;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class TestPhotographerViewModel {

    @Test
    public void isLastName_InvalidLastName_False() {
        PhotographerViewModel photographerViewModel = new PhotographerViewModel();
        photographerViewModel.setPhotographer(new Photographer("Marius", "", LocalDate.now(), "some notes"));
        Assert.assertFalse("Empty Last Name must be invalid", photographerViewModel.isLastName());
    }

    @Test
    public void isValidDate_InvalidDate_False() {
        PhotographerViewModel photographerViewModel = new PhotographerViewModel();
        Assert.assertFalse("Date after now must be invalid", photographerViewModel.isValidDate(LocalDate.now().plusDays(1)));
    }


}
