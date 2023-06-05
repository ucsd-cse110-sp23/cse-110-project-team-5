/**
 * This code was modified from the original code found at:
 * https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp given to us by Lab7
 */

//package com.journaldev.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class TLSEmail {

	private String fromEmail;
	private String toEmail;
	private String displayName;
	private String password;
	private String smtpHost;
	private String tlsPort;

	private String subject;
	private String body;

	public void setEmailConfig(String fromEmail, String toEmail, String displayName, 
     String password, String smtpHost, String tlsPort, String subject, String body) {
		this.fromEmail = fromEmail;
		this.toEmail = toEmail;
		this.displayName = displayName;
		this.password = password;
		this.smtpHost = smtpHost;
		this.tlsPort = tlsPort;

		this.subject = subject;
		this.body = body;
	}
	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
	public String send() {
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		// props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.host", this.smtpHost);
		// props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.port", this.tlsPort); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		props.put("mail.debug", "true");
		props.put("mail.smtp.ssl.trust", this.smtpHost);
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		return EmailUtil.sendEmail(session, fromEmail, toEmail, displayName, smtpHost, subject, body);
		
	}
	
}