/**
 * This code was modified from the original code found at:
 * https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp given to us by Lab7
 */

// package src.main.java.sayItAssistant;
// package com.journaldev.mail;

//import java.util.*;


import java.util.Date;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// import javax.activation.DataHandler;
// import javax.activation.DataSource;
// import javax.activation.FileDataSource;
// import javax.mail.BodyPart;
// import java.io.UnsupportedEncodingException;
// import javax.mail.MessagingException;
// import javax.mail.Multipart;
// import javax.mail.internet.MimeBodyPart;
// import javax.mail.internet.MimeMultipart;

public class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param displayName
	 * @param smtpHost
	 * @param subject
	 * @param body
	 */
	public static String sendEmail(Session session, String fromEmail, String toEmail, String displayName, String smtpHost, String subject, String body){
		
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress(fromEmail, displayName));

	      msg.setReplyTo(InternetAddress.parse(fromEmail, false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);

	      return "Email successfully sent.";
	    }
	    catch (Exception e) {
	      e.printStackTrace();
		  return "Email error \n"
		  + "SMTP Host: " + smtpHost + "\n";
	    }
	}
}