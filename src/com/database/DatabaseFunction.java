package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.clink.QueryConstant;

public class DatabaseFunction {

	public void deleteRecords(String query) throws Exception {
		try {
			PreparedStatement ps = null;
			Connection con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void loadCsv(String query) throws Exception {
		try {
			PreparedStatement ps = null;
			Connection con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void delete(String query) throws Exception {
		try {
			PreparedStatement ps = null;
			Connection con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void get(String query, String insertQuery, String startDate, String endDate, String tableName) throws Exception {
		PreparedStatement ps = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			Connection con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, startDate);
			ps.setString(2, endDate);
			rs = ps.executeQuery();
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			final int columnCount = resultSetMetaData.getColumnCount();
			pstmt = con.prepareStatement(insertQuery);
			
			final int batchSize = 500;
			int count = 0;
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					pstmt.setObject(i, rs.getObject(i));
				}
				pstmt.addBatch();
				if (++count % batchSize == 0) {
					pstmt.executeBatch();
				}

			}
			pstmt.executeBatch();
		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (ps != null)
				ps.close();
		}
	}

}
