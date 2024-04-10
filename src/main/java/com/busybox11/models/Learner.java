package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.busybox11.Database;

public class Learner {
  private int id;
  private String name;
  private String surname;
  private Promotion promotion;
  private String address;
  private String email;
  private String phone;
  private int absent;
  private boolean isRepresentative;

  public Learner() {
  }

  public Learner(String name, String surname, Promotion promotion, String address, String email, String phone,
      int absent,
      boolean isRepresentative) {
    this.name = name;
    this.surname = surname;
    this.promotion = promotion;
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.absent = absent;
    this.isRepresentative = isRepresentative;
  }

  public Learner(String name, String surname, int promotionId, String address, String email, String phone,
      int absent,
      boolean isRepresentative) {
    this.name = name;
    this.surname = surname;
    try {
      this.promotion = Promotion.initializeFromDb(promotionId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.absent = absent;
    this.isRepresentative = isRepresentative;
  }

  public Learner(int id, String name, String surname, int promotionId, String address, String email, String phone,
      int absent,
      boolean isRepresentative) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    try {
      this.promotion = Promotion.initializeFromDb(promotionId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.absent = absent;
    this.isRepresentative = isRepresentative;
  }

  public static Learner initializeFromId(int id) {
    // Retrieve the learner from the database

    Learner learner = null;

    String learnerQuery = "SELECT * FROM learners WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(learnerQuery);

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          learner = new Learner(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("surname"),
              rs.getInt("promotion_id"),
              rs.getString("address"),
              rs.getString("email"),
              rs.getString("phone"),
              rs.getInt("absent"),
              rs.getBoolean("isRepresentative"));
        } else {
          throw new Exception("No learner found with ID: " + id);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return learner;
  }

  public static List<Learner> getAllLearners() {
    // Retrieve all learners from the database

    List<Learner> learners = new ArrayList<>();

    String learnersQuery = "SELECT * FROM learners";

    try (Connection conn = Database.getConnection()) {
      try (ResultSet rs = conn.createStatement().executeQuery(learnersQuery)) {
        while (rs.next()) {
          learners.add(new Learner(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("surname"),
              rs.getInt("promotion_id"),
              rs.getString("address"),
              rs.getString("email"),
              rs.getString("phone"),
              rs.getInt("absent"),
              rs.getBoolean("isRepresentative")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return learners;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public Promotion getPromotion() {
    return promotion;
  }

  public String getAddress() {
    return address;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public int getAbsent() {
    return absent;
  }

  public boolean isRepresentative() {
    return isRepresentative;
  }

  public void setAbsent(int absent) {
    this.absent = absent;
  }

  public void insertIntoDB() throws Exception {
    // Create the learner in the database

    // Check that there isn't any other learner with the same name and surname, or
    // email
    String checkQuery = "SELECT * FROM learners WHERE name = ? AND surname = ? OR email = ?";
    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(checkQuery);

      stmt.setString(1, name);
      stmt.setString(2, surname);
      stmt.setString(3, email);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          // Check if both name and surname OR email are already in the database
          // A result doesn't mean that the learner already exists
          if (rs.getString("name").equals(name) && rs.getString("surname").equals(surname)) {
            throw new Exception("Vous ne pouvez pas créer un apprenant avec un nom et prénom déjà existant");
          } else if (rs.getString("email").equals(email)) {
            throw new Exception("Vous ne pouvez pas créer un apprenant avec un email déjà existant");
          }
        }
      }
    }

    // Create the learner
    String learnerQuery = """
        INSERT INTO learners (name, surname, promotion_id, address, email, phone, absent, isRepresentative)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(learnerQuery, Statement.RETURN_GENERATED_KEYS);

      // Execute the query
      stmt.setString(1, name);
      stmt.setString(2, surname);
      stmt.setInt(3, promotion.getId());
      stmt.setString(4, address);
      stmt.setString(5, email);
      stmt.setString(6, phone);
      stmt.setInt(7, absent);
      stmt.setInt(8, isRepresentative ? 1 : 0);

      stmt.executeUpdate();

      // Get the generated ID
      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt(1);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateInDB() {
    // Update the learner in the database

    String learnerQuery = """
        UPDATE learners
        SET name = ?, surname = ?, promotion_id = ?, address = ?, email = ?, phone = ?, absent = ?, isRepresentative = ?
        WHERE id = ?;
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(learnerQuery);

      // Execute the query
      stmt.setString(1, name);
      stmt.setString(2, surname);
      stmt.setInt(3, promotion.getId());
      stmt.setString(4, address);
      stmt.setString(5, email);
      stmt.setString(6, phone);
      stmt.setInt(7, absent);
      stmt.setInt(8, isRepresentative ? 1 : 0);
      stmt.setInt(9, id);

      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteFromDB() {
    // Delete the learner from the database

    String learnerQuery = "DELETE FROM learners WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(learnerQuery);

      // Execute the query
      stmt.setInt(1, id);

      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
