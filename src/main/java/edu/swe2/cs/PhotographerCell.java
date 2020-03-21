package edu.swe2.cs;

import edu.swe2.cs.model.Photographer;
import javafx.scene.control.ListCell;

public class PhotographerCell extends ListCell<Photographer> {

    @Override
    public void updateItem(Photographer item, boolean empty) {
        super.updateItem(item, empty);
        StringBuilder stringBuilder = new StringBuilder();
        if (item != null && !empty) {
            stringBuilder.append(item.getId());
            stringBuilder.append(". ");
            stringBuilder.append(item.getLastName());
            stringBuilder.append(", ");
            stringBuilder.append(item.getFirstName());
        }
        this.setText(stringBuilder.toString());
    }
}
