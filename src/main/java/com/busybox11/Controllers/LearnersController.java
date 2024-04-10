package com.busybox11.Controllers;

import java.util.List;

import com.busybox11.models.Learner;

public class LearnersController {
  public void learnerMenu(int id) {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      // Retrieve the learner from the database
      Learner learner = Learner.initializeFromId(id);

      if (learner == null) {
        return;
      }

      System.out.println("Détails de l'apprenant");
      System.out.println("\n-----------\n");

      System.out.println("Nom       : " + learner.getName());
      System.out.println("Prénom    : " + learner.getSurname());
      System.out.println("Promotion : " + learner.getPromotion().getName());
      System.out.println("Adresse   : " + learner.getAddress());
      System.out.println("Email     : " + learner.getEmail());
      System.out.println("Téléphone : " + learner.getPhone());
      System.out.println("Absences  : " + learner.getAbsent());
      System.out.println("Délégué   : " + (learner.isRepresentative() ? "Oui" : "Non"));

      System.out.println("\n-----------\n");

      System.out.println("1. Modifier le nombre d'absences");
      if (!learner.isRepresentative()) {
        System.out.println("2. Supprimer l'apprenant");
      }
      System.out.println("\n0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      switch (choice) {
        case 1:
          System.out.print("Nouveau nombre d'absences : ");
          int absent = Integer.parseInt(System.console().readLine());
          learner.setAbsent(absent);
          learner.updateInDB();
          break;
        case 2:
          if (!learner.isRepresentative()) {
            learner.deleteFromDB();
          }
          break;
        case 0:
          return;
        default:
          System.out.println("Choix invalide");
          break;
      }
    }
  }

  private void chooseLearnerMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Choisir un apprenant");

      System.out.println("\n-----------\n");

      // Retrieve all learners from the database
      List<Learner> learners = Learner.getAllLearners();

      for (Learner learner : learners) {
        System.out.println(learner.getId() + ". " + learner.getName() + " " + learner.getSurname());
      }

      System.out.println("\n-----------\n");

      System.out.println("0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      if (choice == 0) {
        return;
      }

      learnerMenu(choice);
    }
  }

  public void sortLearnersMenu(boolean byAbsences) {
    System.out.print("\033[H\033[2J");
    System.out.flush();

    System.out.println("Apprenants par " + (byAbsences ? "absences décroissantes " : "nom"));

    System.out.println("\n-----------\n");

    // Retrieve all learners from the database
    List<Learner> learners = Learner.getAllLearners();

    if (byAbsences) {
      learners.sort((l1, l2) -> l2.getAbsent() - l1.getAbsent());
    } else {
      learners.sort((l1, l2) -> l1.getName().compareTo(l2.getName()));
    }

    for (Learner learner : learners) {
      System.out.println(learner.getId() + ". " + learner.getName() + " " + learner.getSurname() + " : "
          + learner.getAbsent() + " absences");
    }

    System.out.println("\n-----------\n");

    System.out.print("\nAppuyez sur Entrée pour continuer...");

    System.console().readLine();

    return;
  }

  public void mainMenu() {
    while (true) {
      System.out.print("\033[H\033[2J");
      System.out.flush();

      System.out.println("Menu des apprenants");
      System.out.println("\n-----------\n");

      System.out.println("1. Voir les détails d'un apprenant");
      System.out.println("2. Trier les apprenants par nom");
      System.out.println("3. Trier les apprenants par absences");
      System.out.println("4. Ajouter un apprenant");
      System.out.println("\n0. Retour");

      System.out.print("\nChoix : ");
      int choice = Integer.parseInt(System.console().readLine());

      switch (choice) {
        case 1:
          chooseLearnerMenu();
          break;
        case 2:
          sortLearnersMenu(false);
          break;
        case 3:
          sortLearnersMenu(true);
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
