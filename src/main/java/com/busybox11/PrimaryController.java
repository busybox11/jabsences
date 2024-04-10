package com.busybox11;

import java.io.IOException;
import java.util.List;

import com.busybox11.models.Promotion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PrimaryController {
    @FXML
    VBox allPromotions;

    @FXML
    protected void initialize() {
        // Get all promotions from the database
        // For each promotion, create a new PromotionCard and add it to the
        // allPromotions VBox
        List<Promotion> promotions = Promotion.getAllPromotions();

        for (Promotion promotion : promotions) {
            // Create a new button with the promotion name
            Button button = new Button(promotion.getName());
            allPromotions.getChildren().add(button);
        }
    }

    @FXML
    private void showCreatePromotion() throws IOException {
        App.setRoot("createPromotion");
    }
}
