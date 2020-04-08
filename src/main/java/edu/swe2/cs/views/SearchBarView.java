package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.SearchBarViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class SearchBarView {

    @FXML
    public TextField searchTextField;
    @FXML
    public Button searchButton;
    @FXML
    public HBox hBox;

    private SearchBarViewModel viewModel;

    @FXML
    private void initialize() {
        this.searchTextField.textProperty().bindBidirectional(viewModel.searchTextProperty());
        Tooltip tooltip = new Tooltip("Empty Search will query all Pictures");
        tooltip.setShowDelay(Duration.seconds(0.1));
        tooltip.setShowDuration(Duration.seconds(2));
        tooltip.setStyle("-fx-background-color:azure; -fx-text-fill:black;");
        Tooltip.install(hBox, tooltip);
    }

    public SearchBarView() {
        this.viewModel = new SearchBarViewModel();
    }

    @FXML
    public void onSearch() {
        viewModel.search();
    }


}
