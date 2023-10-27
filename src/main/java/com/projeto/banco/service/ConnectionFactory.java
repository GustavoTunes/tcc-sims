package com.projeto.banco.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static final String DATABASE_URL = "jdbc:mysql://localhost/SIMS";
	private static final String DATABASE_USER = "root";
	private static final String DATABASE_PASSWORD = "1234";

	public static Connection getConnection() throws SQLException {
		try {
			System.out.println("foi?");
			return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
			
		} catch (SQLException e) {
			throw e;
		}
	}
}
