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
    Promotion promotion2 = new Promotion("Promotion 2");

    promotion.insertIntoDB();
    promotion2.insertIntoDB();
    assertEquals(1, promotion.getId());
    assertEquals(2, promotion2.getId());

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

    Learner learner3 = new Learner("Jean", "Dupont", promotion2, "Rue de la Paix", "jdupont@gmail.com",
        "06 28 74 91 74",
        11, false);
    Learner learner4 = new Learner("Samuel", "Etienne", promotion2, "Avenue des Champions", "samuel.etienne@france.tv",
        "01 23 45 67 89", 0, true);

    // Insert learners
    learner1.insertIntoDB();
    learner2.insertIntoDB();
    learner3.insertIntoDB();
    learner4.insertIntoDB();

    // Retrieve learners
    Learner dbLearner1 = Learner.initializeFromId(1);
    assertEquals(1, dbLearner1.getId());

    Learner dbLearner2 = Learner.initializeFromId(2);
    assertEquals(2, dbLearner2.getId());

    Learner dbLearner3 = Learner.initializeFromId(3);
    assertEquals(3, dbLearner3.getId());

    Learner dbLearner4 = Learner.initializeFromId(4);
    assertEquals(4, dbLearner4.getId());
  }
}
