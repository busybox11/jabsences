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

    try {
      promotion.insertIntoDB();
      promotion2.insertIntoDB();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    try {
      learner1.insertIntoDB();
      learner2.insertIntoDB();
      learner3.insertIntoDB();
      learner4.insertIntoDB();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Retrieve learners
    Learner dbLearner1 = Learner.initializeFromId(1);
    assertEquals(1, dbLearner1.getId());

    Learner dbLearner2 = Learner.initializeFromId(2);
    assertEquals(2, dbLearner2.getId());

    Learner dbLearner3 = Learner.initializeFromId(3);
    assertEquals(3, dbLearner3.getId());

    Learner dbLearner4 = Learner.initializeFromId(4);
    assertEquals(4, dbLearner4.getId());

    // Try to create promotion with same name, expect error
    Promotion promotion3 = new Promotion("Promotion 1");
    Exception exception = assertThrows(Exception.class, () -> {
      promotion3.insertIntoDB();
    });

    String expectedMessage = "Une promotion avec le même nom existe déjà";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    // Try to create learner with same name and surname, expect error
    Learner learner5 = new Learner("Patrick", "Balkany", promotion, "92044 Levallois-Perret", "patrick@riches.club",
        "01 47 15 47 15", 93, false);
    Exception exception2 = assertThrows(Exception.class, () -> {
      learner5.insertIntoDB();
    });

    String expectedMessage2 = "Vous ne pouvez pas créer un apprenant avec un nom et prénom déjà existant";
    String actualMessage2 = exception2.getMessage();

    assertTrue(actualMessage2.contains(expectedMessage2));

    // Try to create learner with same email, expect error
    Learner learner6 = new Learner("Jeanne", "Dupont", promotion2, "Rue de la Paix", "jdupont@gmail.com",
        "0693947205",
        2, false);
    Exception exception3 = assertThrows(Exception.class, () -> {
      learner6.insertIntoDB();
    });

    String expectedMessage3 = "Vous ne pouvez pas créer un apprenant avec un email déjà existant";
    String actualMessage3 = exception3.getMessage();

    assertTrue(actualMessage3.contains(expectedMessage3));
  }
}
