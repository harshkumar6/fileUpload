package com.clink;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailService {
	private static String fromEmail = "support@kalbos.io"; 
	private static String password = "mk@ggn1kal"; 

	public Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		return session;

	}

	public static StringBuffer sendEmail(String toEmail, String subject, String body) {
		StringBuffer buff = new StringBuffer();
		try {
			SendMailService ser = new SendMailService();
			MimeMessage msg = new MimeMessage(ser.getSession());
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("support@kalbos.io", "KALBOS"));
			msg.setReplyTo(InternetAddress.parse("support@kalbos.io", false));
			msg.setSubject(subject, "UTF-8");
			msg.setContent(body, "text/html");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			Transport.send(msg);
			buff.append("mail sent successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			buff.append("Error occured while sending mail:-" + e.getMessage());
		}
		return buff;
	}
}
