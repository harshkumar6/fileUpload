package com.clink;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Centurylink90DaysData
 */
@WebServlet("/Centurylink90DaysData")
public class Centurylink90DaysData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Centurylink90DaysData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String type = (String) request.getParameter("type");
		String startDate = (String) request.getParameter("fromDate");
		String endDate = (String) request.getParameter("toDate");
		if (type == null || type.isEmpty() || startDate == null || startDate.isEmpty() || endDate == null
				|| endDate.isEmpty()) {
			out.print("Type/StartDate/EndDate cann't be null.");
		} else {
			String tableName = "centurylinkdata_MTD_Autofail";
			String query = QueryConstant.INSERT_QUERY_30_DAYS;
			if (type.equalsIgnoreCase("90days")) {
				tableName = "centurylinkdata_90_Days_Rolling";
				query = QueryConstant.INSERT_QUERY_90_DAYS;
			}
			com.database.DatabaseFunction dbf = new com.database.DatabaseFunction();
			try {
				dbf.deleteRecords("TRUNCATE " + tableName);
				dbf.get(QueryConstant.GET_QUERY, query, startDate, endDate, tableName);
			} catch (Exception e) {
				e.printStackTrace();
				out.print("exception occured:-" + e.getMessage());
			}
		}
		out.print("records inserted successfully.");
		response.setContentType("text/html");
		RequestDispatcher rd = request.getRequestDispatcher("./insertData.html");
		rd.include(request, response);

	}

}
