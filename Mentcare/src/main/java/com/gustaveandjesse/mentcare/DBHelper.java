package com.gustaveandjesse.mentcare;

import java.sql.*;

public class DBHelper {
    private static DBHelper instance;
    private final String DB_URL = "jdbc:sqlite:mentcare.db";

    private DBHelper() { }

    public static synchronized DBHelper getInstance() {
        if (instance == null) instance = new DBHelper();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            String staff = "CREATE TABLE IF NOT EXISTS staff (" +
                    "staff_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL, role TEXT NOT NULL, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL);";
            String patients = "CREATE TABLE IF NOT EXISTS patients (" +
                    "patient_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL, dob TEXT, gender TEXT, contact TEXT, summary TEXT, last_visit TEXT);";
            String appts = "CREATE TABLE IF NOT EXISTS appointments (" +
                    "appointment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "patient_id INTEGER, date TEXT, time TEXT, status TEXT, notes TEXT, " +
                    "FOREIGN KEY(patient_id) REFERENCES patients(patient_id));";
            s.execute(staff);
            s.execute(patients);
            s.execute(appts);
            // Insert default admin if not exists
            String insertAdmin = "INSERT INTO staff(name,role,username,password) " +
                    "SELECT 'Admin','Administrator','admin','admin' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM staff WHERE username='admin');";
            s.execute(insertAdmin);
            System.out.println("DB initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
