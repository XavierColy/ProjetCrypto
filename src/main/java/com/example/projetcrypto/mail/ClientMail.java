package com.example.projetcrypto.mail;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class ClientMail {
	
	private String user;
	
	public static Session connectServeurMail(String user, String password) {
		
		 Properties properties = new Properties();  
         properties.put("mail.smtp.auth", "true");
         properties.put("mail.smtp.starttls.enable", "true");
         properties.put("mail.smtp.host", "smtp.outlook.com");
         properties.put("mail.smtp.port", "587");
         Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
        	 protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user,password);
			} 
         });
         try {
             // Attempt to connect and authenticate to the email server
             Transport transport = session.getTransport("smtp");
             transport.connect();
             System.out.println("Authentication succeeded.");
             transport.close();
         } catch (MessagingException e) {
             System.out.println("Authentication failed: " + e.getMessage());
         }
         return session;
	}
	
	public void envoyerMessage(Session session, String destination,String subject, String messageContent) {
		try{
			MimeMessage message =new MimeMessage(session);
		    message.setFrom(user);
		    //!!!!
		    message.setText("Bonjour, \n ceci est mon premier mail depuis javamail ...");
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
		    message.setSubject("mon premier email ..");
		    Transport.send(message);
		    
		} catch (NoSuchProviderException e) {e.printStackTrace();
		} catch (MessagingException e) {e.printStackTrace();
		}	
	}
	
	public void envoyerMessage(Session session, String destination,String subject, String messageContent,String [] attachementPaths) {
		try{ 
		      MimeMessage message=new MimeMessage(session);
		     message.setFrom(user);
		     message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
		     message.setSubject("mon premier email avec piece jointe..");
		     
		     Multipart myemailcontent=new MimeMultipart();
		     MimeBodyPart bodypart=new MimeBodyPart();
		     bodypart.setText("ceci est un test de mail avec piece jointe ...");
		     myemailcontent.addBodyPart(bodypart);
		     
		     for(String attachementPath : attachementPaths ) {
		    	 MimeBodyPart attachementfile = new MimeBodyPart();
			     attachementfile.attachFile(attachementPath);
			     myemailcontent.addBodyPart(attachementfile);
		     }
		     
		     message.setContent(myemailcontent);
		     Transport.send(message);

		  } catch (NoSuchProviderException e) {e.printStackTrace();
		  } catch (MessagingException e) {System.out.println("Error sending message: " + e.getMessage());
		  } catch (IOException ex) {  
		         Logger.getLogger(ClientMail.class.getName()).log(Level.SEVERE, null, ex);
		  }
	
	}
	
	
	
	
	
	
	public static void main(String[] args)  {  
		  
		  //String host = "outlook.office365.com";//change accordingly  
		  String username= "cryptotest10@outlook.com";  
		  String password= "Azerty@2023";//change accordingly  
		   
		  
		   String path="C:\\Users\\USER\\Pictures\\image65.jpg";
		   String user2 = "koffigiovanni.allaglo@uphf.fr";
		   //sendmessagewithattachement(username, password,user2,path);
		     connectServeurMail(username, password);
		     System.out.println("message sent ...");
		     
		     Scanner sc=new Scanner(System.in);
		     System.out.println("type something ....");
		     
		     sc.nextLine();
		     
		     //downloadEmailAttachments(username, password);
		  
		}  
	
	
	
	
}


