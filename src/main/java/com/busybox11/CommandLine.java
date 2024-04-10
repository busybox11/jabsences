package com.busybox11;

import java.util.List;

import com.busybox11.models.Learner;
import com.busybox11.models.Promotion;

public class CommandLine {
  public static void main(String[] args) {
    List<Promotion> promotions = Promotion.getAllPromotions();
    for (Promotion promotion : promotions) {
      List<Learner> learners = promotion.getAllLearners();

      System.out.println(promotion.getName());

      for (Learner learner : learners) {
        System.out.println("  " + learner.getName() + " " + learner.getSurname());
      }
    }
  }
}
