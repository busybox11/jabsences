package com.busybox11.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.busybox11.Database;

public class Absence {
  private int id;
  private Learner learner;
  private Course course;
  private String reason;

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
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
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
        INSERT INTO absences (learner_id, course_id, reason) VALUES (%d, %d, '%s');
        """;

    try (Connection conn = Database.getConnection()) {
      // Prepare the statement
      PreparedStatement stmt = conn.prepareStatement(absenceQuery);

      // Set the values
      stmt.setInt(1, learner.getId());
      stmt.setInt(2, course.getId());
      stmt.setString(3, reason);

      // Execute the query
      stmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
