/**
 * This code was modified from the original code found at:
 * https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp given to us by Lab7
 */

//package com.journaldev.mail;
// import java.io.File;
// import java.util.Scanner;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.FileOutputStream;
// import java.io.FileNotFoundException;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

// 

public class TLSEmail {

	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
	public String send() {

        File setup = new File("src/main/java/sayItAssistant/EmailSetupInfo.txt");
        String setupInfo[] = new String[7];
        try{
            Scanner myReader = new Scanner(setup);
            int i = 0;
            while (myReader.hasNextLine()) {
                setupInfo[i] = myReader.nextLine();
                i++;
            }
            System.out.println(setupInfo[0]);
            myReader.close();
        }
        catch (FileNotFoundException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
        
		final String fromEmail = "sups1237@gmail.com"; //requires valid gmail id
		final String password = "ljkldeuetmesjlql"; // correct password for gmail id
		final String toEmail = "xicoreyes513@gmail.com"; // can be any email id 
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		props.put("mail.debug", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		return EmailUtil.sendEmail(session, toEmail,"TLSEmail Testing Subject", "TLSEmail Testing Body");
		
	}
	
}