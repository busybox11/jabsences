package com.busybox11.Controllers;

import java.util.List;

import com.busybox11.models.Promotion;

public class MainMenuController {
  public void mainMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Bienvenue !");

      System.out.println("\n-----------\n");

      // Get list of promotions
      // If there is at least one, show menu for learners
      List<Promotion> promotions = Promotion.getAllPromotions();

      if (promotions.size() > 0) {
        System.out.println("1. Menu des apprenants");
      } else {
        System.out.println("1. Menu apprenant désactivé : aucune promotion n'a été créée");
      }
      System.out.println("2. Menu des promotions");
      System.out.println("\n0. Quitter");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      switch (choice) {
        case 1:
          if (promotions.size() == 0) {
            break;
          }
          LearnersController learnersController = new LearnersController();
          learnersController.mainMenu();
          break;
        case 2:
          PromotionsController promotionsController = new PromotionsController();
          promotionsController.mainMenu();
          break;
        case 0:
          System.out.println("Au revoir !");
          return;
        default:
          System.out.println("Choix invalide");
          break;
      }
    }
  }
}
