package com.busybox11.models;

import org.junit.Test;

import com.busybox11.Database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;

public class PromotionTest {
  @BeforeClass
  public static void resetDatabase() {
    Database.resetAndInitializeTables();
  }

  @Test
  public void testPromotion() {
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
  }
}
