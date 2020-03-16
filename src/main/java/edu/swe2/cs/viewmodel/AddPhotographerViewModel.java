package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.Photographer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class AddPhotographerViewModel {

    private Photographer photographer;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<LocalDate> birthDate;
    private StringProperty notes;
    private StringProperty labelBirthDate;


    public AddPhotographerViewModel() {
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        birthDate = new SimpleObjectProperty<>();
        birthDate.addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                if (!isValidDate(t1)) {
                    labelBirthDate.set("Warning: Birthdate has to be before today");
                } else {
                    labelBirthDate.set("Birthdate");
                }
            }
        });
        notes = new SimpleStringProperty("");
        labelBirthDate = new SimpleStringProperty("Birthdate");
    }

    public StringProperty getLastName() {
        return lastName;
    }

    public StringProperty getFirstName() {
        return firstName;
    }

    public ObjectProperty<LocalDate> getBirthDate() {
        return birthDate;
    }

    public StringProperty getNotes() {
        return notes;
    }

    public StringProperty getLabelBirthDate() {
        return labelBirthDate;
    }

    public boolean isValid() {
        return (isValidDate(birthDate.getValue()) && isLastName());
    }

    public boolean isLastName() {
        if (lastName == null) {
            return false;
        }
        return !(lastName.getValue().isEmpty()) && !(lastName.getValue().equals(""));
    }

    public boolean isValidDate(LocalDate date) {
        if (date == null) {
            return true;
        }
        return !(date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()));
    }

}
