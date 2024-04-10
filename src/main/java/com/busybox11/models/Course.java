package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.busybox11.Database;

public class Course {
  private int id;
  private String name;
  private int date;

  public Course(int id, String name, int date) {
    this.id = id;
    this.name = name;
    this.date = date;
  }

  public static Course initializeFromId(int id) {
    // Retrieve the learner from the database

    Course course = null;

    String courseQuery = "SELECT * FROM courses WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(courseQuery);

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          course = new Course(
              rs.getInt("id"),
              rs.getString("name"),
              rs.getInt("date"));
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return course;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getDate() {
    return date;
  }

  public void insertIntoDB() {
    // Create the course in the database

    String courseQuery = """
        INSERT INTO courses (name, date) VALUES (?, ?);
        """;

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(courseQuery);

      stmt.setString(1, name);
      stmt.setInt(2, date);

      stmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
