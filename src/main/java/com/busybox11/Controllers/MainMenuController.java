package com.busybox11.Controllers;

public class MainMenuController {
  public void mainMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Bienvenue !");

      System.out.println("\n-----------\n");

      System.out.println("1. Menu des apprenants");
      System.out.println("2. Menu des promotions");
      System.out.println("\n0. Quitter");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      switch (choice) {
        case 1:
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
