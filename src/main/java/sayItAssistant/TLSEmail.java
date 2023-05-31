
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

	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
	public static void main(String[] args) {

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
        
		final String fromEmail = setupInfo[5]; //requires valid gmail id
		final String password = setupInfo[6]; // correct password for gmail id
		final String toEmail = "kec020@ucsd.edu"; // can be any email id 
		
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		EmailUtil.sendEmail(session, toEmail,"TLSEmail Testing Subject", "TLSEmail Testing Body");
		
	}

	
}