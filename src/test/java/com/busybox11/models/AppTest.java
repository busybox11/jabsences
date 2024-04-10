package com.busybox11.models;

import org.junit.Test;

import com.busybox11.Database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;

public class AppTest {
  @BeforeClass
  public static void resetDatabase() {
    Database.resetAndInitializeTables();
  }

  @Test
  public void mainTest() {
    Promotion promotion = new Promotion(1, "Promotion 1");
    assertEquals(1, promotion.getId());
    assertEquals("Promotion 1", promotion.getName());

    // Insert
    promotion.insertIntoDB();
    assertEquals(1, promotion.getId());

    // Retrieve
    Promotion dbPromotion = Promotion.initializeFromDb(1);
    assertEquals(1, dbPromotion.getId());
    assertEquals("Promotion 1", dbPromotion.getName());

    // Create learners
    Learner learner1 = new Learner("Patrick", "Balkany", promotion, "92044 Levallois-Perret", "patocheb@riches.club",
        "01 47 15 47 15", 93, false);

    assertEquals("Patrick", learner1.getName());
    assertEquals("Balkany", learner1.getSurname());
    assertEquals(promotion, learner1.getPromotion());

    Learner learner2 = new Learner("Jean", "Valjean", promotion, "Rue de la Chanvrerie", "jvaljean@vieux.community",
        "01 23 45 67 89", 0, true);
    Learner learner3 = new Learner("Jean", "Dupont", promotion, "Rue de la Paix", "jdupont@gmail.com", "06 28 74 91 74",
        11, false);

    // Insert learners
    learner1.insertIntoDB();
    learner2.insertIntoDB();
    learner3.insertIntoDB();
  }
}
