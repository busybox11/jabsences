package com.busybox11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  public static void resetTables() {
    String[] tables = { "promotions", "learners", "courses", "absences" };

    try (Connection conn = Database.connect()) {
      for (String table : tables) {
        conn.createStatement().execute("DROP TABLE IF EXISTS " + table);
      }

      System.out.println("Tables reset.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void initializeTables() {
    String promotionTable = """
          CREATE TABLE IF NOT EXISTS promotions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL
          );
        """;

    String learnersTable = """
          CREATE TABLE IF NOT EXISTS learners (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            surname TEXT NOT NULL,
            promotion_id INTEGER NOT NULL,
            address TEXT NOT NULL,
            email TEXT NOT NULL,
            phone TEXT NOT NULL,
            isRepresentative INTEGER NOT NULL,
            FOREIGN KEY (promotion_id) REFERENCES promotions(id)
          );
        """;

    String coursesTable = """
          CREATE TABLE IF NOT EXISTS courses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            date INT NOT NULL
          );
        """;

    String absencesTable = """
          CREATE TABLE IF NOT EXISTS absences (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            learner_id INTEGER NOT NULL,
            course_id INTEGER NOT NULL,
            reason TEXT,
            FOREIGN KEY (learner_id) REFERENCES learners(id),
            FOREIGN KEY (course_id) REFERENCES courses(id)
          );
        """;

    String[] tables = { promotionTable, learnersTable, coursesTable, absencesTable };

    try (Connection conn = Database.connect()) {
      for (String table : tables) {
        conn.createStatement().execute(table);
      }

      System.out.println("Tables initialized.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static Connection connect() {
    Connection conn = null;
    try {
      String path = System.getProperty("user.home") + "/jabsences";

      // Create the directory if it doesn't exist
      java.io.File dir = new java.io.File(path);
      dir.mkdir();

      // DB connection string
      String url = "jdbc:sqlite:" + path + "/jabsences.db";

      // Create a connection to the database
      conn = DriverManager.getConnection(url);

      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return conn;
  }

  public static Connection getConnection() {
    return connect();
  }

  public static void resetAndInitializeTables() {
    resetTables();
    initializeTables();
  }

  public static void main(String[] args) {
    resetAndInitializeTables();
  }
}
