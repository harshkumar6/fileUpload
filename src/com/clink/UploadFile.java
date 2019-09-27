package com.clink;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.database.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Connection;

import clink.DFRFilesProcess;
import clink.FeedsFIlesProcess;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
		maxFileSize = 1024 * 1024 * 1000, // 50 MB
		maxRequestSize = 1024 * 1024 * 1000)
public class UploadFile extends HttpServlet {

	public static final String DFR_FILE_NAME = "DFR";
	public static final String FEED_FILE_NAME = "Feed";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		StringBuffer buff = new StringBuffer();
		try {
			String filePath = request.getParameter("filePath");

			String errorMessage = null;
			String fileName = null;
			if (filePath == null) {
				buff.append("filePath cann't be null.");
				return;
			}
			File folder = new File(filePath);
			if (!folder.exists()) {
				buff.append("filePath is invalid");
				return;
			}
			File[] file = folder.listFiles();
			if (file.length > 0) {
				for (final File fileEntry : folder.listFiles()) {
					System.out.println(fileEntry.getName());
					fileName = fileEntry.getName();
					try {
						errorMessage = validateFileExtension(fileName, "CSV");
						if (errorMessage == null) {
							break;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						buff.append("something went wrong:- " + e.getMessage());
						return;
					}
				}
			} else {
				buff.append("folder is not empty.");
				return;
			}
			if (errorMessage != null) {
				buff.append(errorMessage);
				return;
			}
			filePath = filePath.concat("/" + fileName);
			errorMessage = processFile(filePath, true);
			if (errorMessage != null) {
				buff.append(errorMessage);
			} else {
				buff.append("file uploaded successfully");
			}
		} finally {
			System.out.println("in finally");
			out.print(buff.toString());
//			HTTPClientUtilityService service = new HTTPClientUtilityService();
//			Map<String, String> headers = new HashMap<String, String>();
//			Map<String, Object> requestBody = new HashMap<String, Object>();
//			try {
//				requestBody.put("messageBody", buff.toString());
//				requestBody.put("messageSubject", "Results Of CSV Upload");
//				requestBody.put("toAddress", Arrays.asList("harsh.kumar1@mattsenkumar.net"));
//				service.postBodyRequestAsString("http://localhost:9090/email/send", requestBody, headers);
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			java.sql.Connection con = null;
			try {
				con = DatabaseConnection.getConnection();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside upload file servlet");
		String UPLOAD_DIRECTORY = "";
		String folderName = null;
		java.util.Date startDate = new java.util.Date();
		// process only if its multipart content
		String serverName = request.getServerName();
		String separator = "\\^";
		String errorMessage = null;
		String message = null;
		if (ServletFileUpload.isMultipartContent(request)) {
			System.out.println("file is of multipart");
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory())
						.parseRequest(new ServletRequestContext(request));

				if (!serverName.equals("localhost")) {
					UPLOAD_DIRECTORY = "/opt/apache-tomcat-9.0.0.M26/webapps/Resources/CenturyLink/";
				}

				folderName = UPLOAD_DIRECTORY + (folderName != null ? folderName : "csv");
				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						String name = item.getName();
						errorMessage = validateFileExtension(name, "CSV");
						if (errorMessage != null)
							break;
						if (item.getName().contains("DFR")) {
							separator = ",";
						}
						InputStream stream = item.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
						String headerLine = reader.readLine();
						String[] headers = headerLine.split(separator);

						if (headers.length != 43 || headers.length != 196) {
							errorMessage = "Invalid file, As file Headers are not valid";
							break;
						}
						File destinationDirectory = new File(folderName);
						if (!destinationDirectory.exists()) {
							destinationDirectory.mkdir();
						}
						folderName = folderName + File.separator + name;
						File destFile = new File(folderName);
						item.write(destFile);
						System.out.println("file created");
					}
				}
				if (errorMessage != null) {
					message = errorMessage;
				} else {
					String error = processFile(folderName, false);
					if (error != null)
						errorMessage = error;
					else
						message = "File uploaded successfully!";
				}

				System.out.println(" start date time = " + startDate);
				System.out.println(" end date time = " + new java.util.Date());

				// File uploaded successfullyfolderName
			} catch (Exception ex) {
				errorMessage = "File Upload Failed due to " + ex;
			}

		} else {
			System.out.println("file is not of multipart");
			errorMessage = "Sorry this Servlet only handles file upload request";
		}
		Map<String, String> data = new HashMap<>();
		ObjectMapper map = new ObjectMapper();
		response.setContentType("text/plain");
		if (errorMessage != null) {
			data.put("errorMessage", errorMessage);
			data.put("status", "500");
		} else {
			data.put("message", message);
			data.put("status", "201");
		}
		String dataStr = map.writeValueAsString(data);
		response.getWriter().write(dataStr);
	}

	private String validateFileExtension(String fileName, String expectedExt) throws Exception {
		String extension = "";
		if (!(fileName.contains(DFR_FILE_NAME) || fileName.contains(FEED_FILE_NAME))) {
			return "file is not of DFR/FEED type.";
		}
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
			if (!extension.equalsIgnoreCase(expectedExt))
				return "FILE is not of " + expectedExt + " type";
		} else
			return "FILE is not of " + expectedExt + " type";
		return null;
	}

	private String processFile(String filePath, Boolean isCheckHeader) {
		// logic to load to mysql
		String errorMessage = null;

		if (filePath.contains(DFR_FILE_NAME)) {
			try {
				errorMessage = DFRFilesProcess.logic1(filePath, isCheckHeader);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "unable to load file in db " + e.getMessage();
			}
		} else if (filePath.contains(FEED_FILE_NAME)) {
			try {
				errorMessage = FeedsFIlesProcess.logic1(filePath, isCheckHeader);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "unable to load file in db " + e.getMessage();
			}
		} else {
			errorMessage = "file is not of DFR/FEED type.";
		}
		return errorMessage;
	}

}
