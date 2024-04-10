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
  private boolean isRepresentative;

  public Learner() {
  }

  public Learner(String name, String surname, Promotion promotion, String address, String email, String phone,
      boolean isRepresentative) {
    this.name = name;
    this.surname = surname;
    this.promotion = promotion;
    this.address = address;
    this.email = email;
    this.phone = phone;
    this.isRepresentative = isRepresentative;
  }

  public Learner(int id, String name, String surname, int promotionId, String address, String email, String phone,
      boolean isRepresentative) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.promotion = Promotion.initializeFromDb(promotionId);
    this.address = address;
    this.email = email;
    this.phone = phone;
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
              rs.getInt("promotionId"),
              rs.getString("address"),
              rs.getString("email"),
              rs.getString("phone"),
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
              rs.getInt("promotionId"),
              rs.getString("address"),
              rs.getString("email"),
              rs.getString("phone"),
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

  public boolean isRepresentative() {
    return isRepresentative;
  }

  public void insertIntoDB() {
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
        // Check if both name and surname OR email are already in the database
        // A result doesn't mean that the learner already exists
        if (rs.getString("name").equals(name) && rs.getString("surname").equals(surname)
            || rs.getString("email").equals(email)) {
          System.out.println("A learner with the same name and surname or email already exists.");
          return;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Create the learner
    String learnerQuery = """
        INSERT INTO learners (name, surname, promotion_id, address, email, phone, isRepresentative)
        VALUES (?, ?, ?, ?, ?, ?, ?);
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
      stmt.setInt(7, isRepresentative ? 1 : 0);

      stmt.executeUpdate();

      // Get the generated ID
      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt(1);
        }
      }

      System.out.println("Learner inserted with ID: " + id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}