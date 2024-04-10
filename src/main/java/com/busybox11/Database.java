package com.busybox11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static void initializeTables() {
        String learnersTable = """
            CREATE TABLE IF NOT EXISTS learners (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                surname TEXT NOT NULL,
                promotion TEXT NOT NULL,
                address TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT NOT NULL,
                isRepresentative INTEGER NOT NULL
            );
        """;

        String coursesTable = """
            CREATE TABLE IF NOT EXISTS courses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                date TEXT NOT NULL
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

        String[] tables = {learnersTable, coursesTable, absencesTable};

        try (Connection conn = Database.connect()) {
            for (String table : tables) {
                conn.createStatement().execute(table);
            }

            System.out.println("Tables initialized.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) {
        Database.initializeTables();
    }
}
