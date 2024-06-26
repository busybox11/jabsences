package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.busybox11.Database;

public class Promotion {
  private int id;
  private String name;

  public Promotion() {
  }

  public Promotion(String name) {
    this.name = name;
  }

  public Promotion(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static Promotion initializeFromDb(int id) {
    // Retrieve the promotion from the database

    Promotion promotion = null;

    String promotionQuery = "SELECT * FROM promotions WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(promotionQuery);

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          promotion = new Promotion(
              rs.getInt("id"),
              rs.getString("name"));
        } else {
          throw new Exception("No promotion found with ID: " + id);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return promotion;
  }

  public static List<Promotion> getAllPromotions() {
    // Retrieve all promotions from the database

    List<Promotion> promotions = new ArrayList<>();

    String promotionQuery = "SELECT * FROM promotions";

    try (Connection conn = Database.getConnection()) {
      try (ResultSet rs = conn.createStatement().executeQuery(promotionQuery)) {
        while (rs.next()) {
          promotions.add(new Promotion(
              rs.getInt("id"),
              rs.getString("name")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return promotions;
  }

  public List<Learner> getAllLearners() {
    // Retrieve all learners for this promotion

    List<Learner> learners = new ArrayList<>();

    String learnersQuery = "SELECT id FROM learners WHERE promotion_id = ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(learnersQuery);

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          learners.add(Learner.initializeFromId(rs.getInt("id")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return learners;
  }

  public static List<Promotion> searchWithName(String name) {
    // Search for learners with a specific name in this promotion

    List<Promotion> promotion = new ArrayList<>();

    String promotionsQuery = "SELECT * FROM promotions WHERE name LIKE ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(promotionsQuery);

      stmt.setString(2, "%" + name + "%");

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          promotion.add(new Promotion(
              rs.getInt("id"),
              rs.getString("name")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return promotion;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void insertIntoDB() throws Exception {
    // Create the promotion in the database

    // Check that there isn't any other promotion with the same name
    String checkQuery = "SELECT * FROM promotions WHERE name = ?";
    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(checkQuery);

      stmt.setString(1, name);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          throw new Exception("Une promotion avec le même nom existe déjà");
        }
      }
    }

    String promotionQuery = """
        INSERT INTO promotions (name) VALUES (?);
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(promotionQuery, Statement.RETURN_GENERATED_KEYS);

      // Execute the query
      stmt.setString(1, name);

      stmt.executeUpdate();

      // Get the generated ID
      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt(1);
        }
      }
    }
  }

  public void updateInDB() {
    // Update the promotion in the database

    String promotionQuery = """
        UPDATE promotions
        SET name = ?
        WHERE id = ?;
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(promotionQuery);

      // Execute the query
      stmt.setString(1, name);
      stmt.setInt(2, id);

      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteFromDB() {
    // Delete the promotion from the database

    String promotionQuery = "DELETE FROM promotions WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(promotionQuery);

      // Execute the query
      stmt.setInt(1, id);

      stmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
