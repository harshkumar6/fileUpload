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
 * Servlet implementation class SendMailByDate
 */
@WebServlet("/SendMailByDate")
public class SendMailByDate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMailByDate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get the values from for
		PrintWriter out = response.getWriter();
		StringBuffer buff = new StringBuffer();
		String query = "All The Reports Between Date Range <StartDate> and <EndDate> are pushed.";
		String listOfUsers = "ankit.kumar1@mattsenkumar.com,gaurav.tuli@mattsenkumar.com";
		String startDate = (String)request.getParameter("fromDate");
		String endDate = (String)request.getParameter("toDate");
		String user = (String)request.getParameter("emailList");
		if(startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
			out.print("StartDate/EndDate cann't be null.");
		}
		if(user != null && !user.isEmpty()) {
			listOfUsers = user;
		}
		query = query.replace("<StartDate>", startDate);
		query = query.replace("<EndDate>", endDate);
		
		System.out.println(query);
		buff.append(SendMailService.sendEmail(listOfUsers, "Testing", query));
		out.print(buff.toString());
		response.setContentType("text/html"); 
		RequestDispatcher rd=request.getRequestDispatcher("./calendarData.html");  
        rd.include(request, response);  
//        response.sendRedirect("./calendarData.html");
	}

}
