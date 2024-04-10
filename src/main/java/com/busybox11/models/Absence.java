package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.busybox11.Database;

public class Absence {
  private int id;
  private Learner learner;
  private Course course;
  private String reason;

  public Absence() {
  }

  public Absence(Learner learner, Course course, String reason) {
    this.learner = learner;
    this.course = course;
    this.reason = reason;
  }

  public Absence(int id, int learnerId, int courseId, String reason) {
    this.id = id;
    this.learner = Learner.initializeFromId(learnerId);
    this.course = Course.initializeFromId(courseId);
    this.reason = reason;
  }

  public static Absence initializeFromId(int id) {
    // Retrieve the absence from the database

    Absence absence = null;

    String absenceQuery = "SELECT * FROM absences WHERE id = ?";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(absenceQuery);

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          absence = new Absence(
              rs.getInt("id"),
              rs.getInt("learner_id"),
              rs.getInt("course_id"),
              rs.getString("reason"));
        } else {
          throw new Exception("No absence found with ID: " + id);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return absence;
  }

  public int getId() {
    return id;
  }

  public Learner getLearner() {
    return learner;
  }

  public Course getCourse() {
    return course;
  }

  public String getReason() {
    return reason;
  }

  public void insertIntoDB() {
    // Create the absence in the database

    String absenceQuery = """
        INSERT INTO absences (learner_id, course_id, reason) VALUES (?, ?, ?);
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(absenceQuery, Statement.RETURN_GENERATED_KEYS);

      // Set the values
      stmt.setInt(1, learner.getId());
      stmt.setInt(2, course.getId());
      stmt.setString(3, reason);

      // Execute the query
      stmt.executeUpdate();

      // Get the generated ID
      try (ResultSet rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          id = rs.getInt(1);
        }
      }

      System.out.println("Absence inserted with ID: " + id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
