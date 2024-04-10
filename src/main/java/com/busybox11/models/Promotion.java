package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.busybox11.Database;

public class Promotion {
  private int id;
  private String name;

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
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return promotion;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void insertIntoDB() {
    // Create the promotion in the database

    String promotionQuery = """
        INSERT INTO promotions (name) VALUES ('%s');
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(promotionQuery);

      // Execute the query
      stmt.setString(1, name);

      stmt.executeUpdate();

      System.out.println("Learner created.");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
