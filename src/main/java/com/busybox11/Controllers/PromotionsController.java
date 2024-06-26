package com.busybox11.Controllers;

import java.util.List;

import com.busybox11.models.Learner;
import com.busybox11.models.Promotion;

public class PromotionsController {
  public void promotionMenu(int id) {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      Promotion promotion = Promotion.initializeFromDb(id);

      if (promotion == null) {
        return;
      }

      System.out.println("Promotion : " + promotion.getName());
      System.out.println("");

      // Get all learners of the promotion
      List<Learner> learners = promotion.getAllLearners();

      for (Learner learner : learners) {
        System.out.println(learner.getId() + ". " + learner.getName() + " " + learner.getSurname() + " : "
            + learner.getAbsent() + " absences");
      }

      // Show the mean of all absences
      int totalAbsences = 0;
      int totalLearners = learners.size();

      for (Learner learner : learners) {
        totalAbsences += learner.getAbsent();
      }

      System.out.println("\nMoyenne d'absences : " + (totalAbsences / totalLearners));

      System.out.println("\n-----------\n");

      System.out.println("0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      if (choice == 0) {
        return;
      }

      LearnersController learnersController = new LearnersController();
      learnersController.learnerMenu(choice);
    }
  }

  public void choosePromotionMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Liste des promotions");

      System.out.println("\n-----------\n");

      // Retrieve all promotions from the database
      List<Promotion> promotions = Promotion.getAllPromotions();

      for (Promotion promotion : promotions) {
        System.out.println(promotion.getId() + ". " + promotion.getName());
      }

      System.out.println("\n-----------\n");

      System.out.println("\n0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      if (choice == 0) {
        return;
      }

      promotionMenu(choice);
    }
  }

  public void createPromotionMenu() {
    System.out.print("\033[H\033[2J");
    System.out.flush();

    System.out.println("Créer une promotion");

    System.out.println("\n-----------\n");

    System.out.print("Nom : ");
    String name = System.console().readLine();

    try {
      Promotion promotion = new Promotion(name);
      promotion.insertIntoDB();

      System.out.println("Promotion créée");
    } catch (Exception e) {
      System.out.println("Erreur lors de la création de la promotion");
      System.out.println(e.getMessage());
    }

    System.out.print("\nAppuyez sur Entrée pour continuer...");

    System.console().readLine();

    return;
  }

  public void searchPromotionMenu() {
    System.out.print("\033[H\033[2J");
    System.out.flush();

    System.out.println("Rechercher une promotion");

    System.out.println("\n-----------\n");

    try {
      System.out.print("Nom : ");
      String name = System.console().readLine();

      List<Promotion> promotion = Promotion.searchWithName(name);

      if (promotion == null) {
        System.out.println("Promotion non trouvée");
        System.out.print("\nAppuyez sur Entrée pour continuer...");

        System.console().readLine();

        return;
      }

      for (Promotion p : promotion) {
        System.out.println(p.getId() + ". " + p.getName());
      }
    } catch (Exception e) {
      System.out.println("Erreur lors de la recherche de la promotion");
      System.out.println(e.getMessage());
    }

    System.out.print("\nAppuyez sur Entrée pour continuer...");

    System.console().readLine();

    return;
  }

  public void mainMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Menu des promotions");

      System.out.println("\n-----------\n");

      System.out.println("1. Afficher une promotion");
      System.out.println("2. Créer une promotion");
      System.out.println("3. Chercher une promotion");
      System.out.println("\n0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      switch (choice) {
        case 1:
          choosePromotionMenu();
          break;
        case 2:
          createPromotionMenu();
          break;
        case 3:
          searchPromotionMenu();
          break;
        case 0:
          return;
        default:
          System.out.println("Choix invalide");
          break;
      }
    }
  }
}
