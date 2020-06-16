package edu.swe2.cs.views;

import edu.swe2.cs.PhotographerCellFactory;
import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import edu.swe2.cs.viewmodel.AssignPictureViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AssignPictureView {

    private static final Logger LOG = LogManager.getLogger(AssignPictureView.class);
    private AssignPictureViewModel viewModel;

    @FXML
    ComboBox<Photographer> ComboBoxPhotographers;

    @FXML
    Label ComboBoxLabel;

    @FXML
    TextField CurrentPhotographerLabel;

    @FXML
    Button assign;

    public AssignPictureView() {
        viewModel = new AssignPictureViewModel();
    }

    @FXML
    private void initialize() {
        ComboBoxPhotographers.setItems(viewModel.getPhotographers());
        viewModel.currentPhotographerProperty().bindBidirectional(CurrentPhotographerLabel.textProperty());
        ComboBoxPhotographers.setButtonCell(new PhotographerCellFactory().call(null));
        ComboBoxPhotographers.setCellFactory(new PhotographerCellFactory());
        ComboBoxPhotographers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldPhotographer, newPhotographer) -> {
            viewModel.onPhotographerSelect(newPhotographer);
            //updateLabel(newPhotographer);
            enableAssign();
        });
    }

    public AssignPictureViewModel getViewModel() {
        return viewModel;
    }

    private void updateLabel(Photographer photographer) {
        if (viewModel.isCurrentPhotographer(photographer)) {
            ComboBoxLabel.textProperty().setValue("Select Photographer: (This Photographer is already assigned to this picture)");
        } else {
            if (!ComboBoxLabel.textProperty().getValue().equals("Select Photographer:")) {
                ComboBoxLabel.textProperty().setValue("Select Photographer:");
            }
        }
    }

    public void disableAssign() {
        this.assign.setDisable(true);
    }

    public void enableAssign() {
        this.assign.setDisable(false);
    }

    //TODO: UPDATE ALL PICTURES IN PROGRAM AFTER CHANGES?

    public void assignPicture(ActionEvent actionEvent) {
        Photographer photographer = viewModel.getPhotographer();
        Photographer oldPhotographer = viewModel.getOldPhotographer();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assign Picture to Photographer");
        if (photographer != null && !viewModel.isCurrentPhotographer(photographer)) {
            Picture picture = viewModel.getPicture();
            if (picture != null) {
                PictureBL.getInstance().assignPicture(picture, oldPhotographer, photographer);
                LOG.info("Open new alert dialog - valid assignPicture form");
                alert.setHeaderText("Successfully assigned Picture to " + photographer.getLastName() + " " + photographer.getFirstName());
            }
        } else {
            LOG.info("Open new alert dialog - invalid assignPicture form");
            alert.setHeaderText("Picture is already assigned to " + photographer.getLastName() + " " + photographer.getFirstName());
        }
        alert.show();
        StageManager.getInstance().closeStage(EStage.ASSIGN_PICTURE_STAGE);
    }
}
