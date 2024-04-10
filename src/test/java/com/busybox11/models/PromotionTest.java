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
  }

  @Test
  public void testInsertPromotion() {
    Promotion promotion = new Promotion("Promotion 1");
    promotion.insertIntoDB();
    assertEquals(1, promotion.getId());
  }

  @Test
  public void testInitializeFromDb() {
    Promotion promotion = Promotion.initializeFromDb(1);
    assertEquals(1, promotion.getId());
    assertEquals("Promotion 1", promotion.getName());
  }
}
