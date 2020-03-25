package edu.swe2.cs.views;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.viewmodel.PhotographerViewModel;
import edu.swe2.cs.viewmodel.events.OnEmptyPhotographerListEvent;
import edu.swe2.cs.viewmodel.events.OnPhotographerSelectEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class UpdatePhotographerView implements ISubscriber {

    private final static Logger LOG = LogManager.getLogger(UpdatePhotographerView.class);
    private Photographer photographer;
    private PhotographerViewModel photographerViewModel = new PhotographerViewModel();
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker birthDate;

    @FXML
    private TextArea notes;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    private Label labelBirthDate;

    @FXML
    Label labelLastName;

    @FXML
    private void initialize() {
        firstName.textProperty().bindBidirectional(photographerViewModel.getFirstName());
        lastName.textProperty().bindBidirectional(photographerViewModel.getLastName());
        birthDate.valueProperty().bindBidirectional(photographerViewModel.getBirthDate());
        notes.textProperty().bindBidirectional(photographerViewModel.getNotes());
        birthDate.getEditor().setDisable(true);
        addListeners();
        lockInputFields();
    }

    public UpdatePhotographerView() {
        eventBus.register(this);
    }

    public void addListeners() {
        lastName.textProperty().addListener((observableValue, s, t1) -> {
            if (!photographerViewModel.isLastName()) {
                labelMandatoryLastName();
                disableUpdate();
            } else {
                unlabelMandatoryLastName();
                enableUpdate();
            }
        });

        birthDate.valueProperty().addListener((observableValue, s, t1) -> {
            if (!photographerViewModel.isValidDate(t1)) {
                labelInvalidBirthDate();
                disableUpdate();
            } else {
                unlabelInvalidBirthDate();
                enableUpdate();
            }
        });
    }

    public void lockInputFields() {
        firstName.setEditable(false);
        lastName.setEditable(false);
        birthDate.setEditable(false);
        notes.setEditable(false);
        disableUpdate();
        disableDelete();
    }

    public void unlockInputFields() {
        firstName.setEditable(true);
        lastName.setEditable(true);
        birthDate.setEditable(true);
        notes.setEditable(true);
        enableDelete();
        enableUpdate();
    }

    public void labelMandatoryLastName() {
        labelLastName.textProperty().setValue(labelLastName.textProperty().getValue() + " is mandatory");
        labelLastName.setTextFill(Color.web("#ff0000"));
        lastName.setStyle("-fx-border-color: red");
    }

    public void unlabelMandatoryLastName() {
        labelLastName.textProperty().setValue("Lastname");
        labelLastName.setTextFill(Color.web("#000000"));
        lastName.setStyle("-fx-border-color: green");
    }

    public void labelInvalidBirthDate() {
        labelBirthDate.setText("Warning: Birthdate has to be before today");
        labelBirthDate.setTextFill(Color.web("#ff0000"));
    }

    public void unlabelInvalidBirthDate() {
        labelBirthDate.setText("Birthdate");
        labelBirthDate.setTextFill(Color.web("#000000"));
    }

    public void disableUpdate() {
        update.setDisable(true);
    }

    public void enableUpdate() {
        update.setDisable(false);
    }

    public void disableDelete() {
        delete.setDisable(true);
    }

    public void enableDelete() {
        delete.setDisable(false);
    }

    public void updatePhotographer(ActionEvent actionEvent) {
        if (photographerViewModel.isValid()) {
            photographerViewModel.updatePhotographer();
            photographerViewModel.getPhotographerModel().updatePhotographerModel(photographerViewModel.getPhotographer());
            photographerViewModel.OnPhotographerUpdate();
            LOG.info("Open new alert dialog - valid update photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Photographer");
            alert.setHeaderText("Updating Photographer");
            alert.setContentText("Successfully updated Photographer");
            alert.show();
        } else {
            LOG.info("Open new alert dialog - invalid update photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Photographer");
            alert.setHeaderText("Failed to update Photographer..");
            alert.setContentText("Please check birthdate and verify that lastname is not empty");
            alert.show();
        }
    }

    public void deletePhotographer(ActionEvent actionEvent) {
        photographerViewModel.getPhotographerModel().removePhotographerFromModel(photographerViewModel.getPhotographer());
        photographerViewModel.OnPhotographerDelete();
        LOG.info("Open new alert dialog - deleting photographer");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deleting Photographer");
        alert.setHeaderText("Successfully deleted Photographer!");
        alert.show();
    }

    public void resetFields() {
        firstName.textProperty().setValue("");
        lastName.textProperty().setValue("");
        birthDate.valueProperty().setValue(null);
        notes.textProperty().setValue("");
        lockInputFields();
    }

    @Override
    public void handle(IEvent<?> event) {
        if (event.getClass().equals(OnPhotographerSelectEvent.class)) {
            photographerViewModel.setPhotographer((Photographer) event.getData());
            unlockInputFields();
        } else if (event.getClass().equals(OnEmptyPhotographerListEvent.class)) {
            photographerViewModel.setPhotographer(null);
            resetFields();
        }
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPhotographerSelectEvent.class);
        supportedEvents.add(OnEmptyPhotographerListEvent.class);
        return supportedEvents;
    }
}
