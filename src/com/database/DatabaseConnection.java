package com.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
	private static Connection con = null;

	public static Properties props = new Properties();
	public static synchronized Connection getConnection() throws Exception {

		if (con == null || con.isClosed()) {
			try {
				loadDataBaseProperty();
				Class.forName(props.getProperty("database.driver.name"));
				// Connection con =
				// DriverManager.getConnection("jdbc:mysql://localhost:3306/dbname?useUnicode=yes&characterEncoding=UTF-8","username",
				// "password");
				// this.con=DriverManager.getConnection("jdbc:mysql://172.25.100.15:3306/booking_com1","deploy","D@Kl012H*");
				// this.con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest?useUnicode=true","root","");
				// this.con=DriverManager.getConnection("jdbc:mysql://172.17.5.121:3306/centurylink","root","p@ssw0rd");
				con = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.username"),
						props.getProperty("database.password"));
				// this.con=DriverManager.getConnection("jdbc:mysql://172.25.100.150:3306/clink","clink","Cl1nk#09876");
				System.out.println(" connection successful ");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return con;

	}

	private static void loadDataBaseProperty() throws IOException, URISyntaxException {
		
		//FileInputStream fis = new FileInputStream("/opt/ankit_access/database/database.properties");
		FileInputStream fis = new FileInputStream("/home/harsh.kumar1/Desktop/database.properties");
		props.load(fis);

	}
}
