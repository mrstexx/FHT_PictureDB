package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.IPTCViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class IPTCView {

    @FXML
    Button saveBtn;
    @FXML
    TextField titleField;
    @FXML
    TextArea captionField;
    @FXML
    TextField cityField;

    @FXML
    public void initialize() {
        titleField.textProperty().bindBidirectional(viewModel.titleProperty());
        captionField.textProperty().bindBidirectional(viewModel.captionProperty());
        cityField.textProperty().bindBidirectional(viewModel.cityProperty());
        saveBtn.disableProperty().bindBidirectional(viewModel.disabledProperty());
    }

    public void saveData() {
        viewModel.saveData();
    }

    private IPTCViewModel viewModel;

    public IPTCView() {
        viewModel = new IPTCViewModel();
    }
}
