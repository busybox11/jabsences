package com.busybox11;

import java.io.IOException;

import com.busybox11.models.Promotion;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreatePromotionController {
  @FXML
  private TextField promotionName;

  @FXML
  private void validate() throws IOException {
    // Get text field with it "promotionName"
    String name = promotionName.getText();

    // Create a new promotion object
    Promotion promotion = new Promotion(name);

    try {
      // Insert the promotion into the database
      promotion.insertIntoDB();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Switch back to the primary view
    switchToPrimary();
  }

  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }
}