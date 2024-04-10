package com.busybox11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  public static void deleteDB() {
    // Delete the database file
    String path = System.getProperty("user.home") + "/jabsences/jabsences.db";

    java.io.File db = new java.io.File(path);

    if (db.delete()) {
      System.out.println("Database deleted.");
    } else {
      System.out.println("Failed to delete the database.");
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
            absent INTEGER DEFAULT 0,
            FOREIGN KEY (promotion_id) REFERENCES promotions(id)
          );
        """;

    String[] tables = { promotionTable, learnersTable };

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
    deleteDB();
    initializeTables();
  }

  public static void main(String[] args) {
    resetAndInitializeTables();
  }
}
