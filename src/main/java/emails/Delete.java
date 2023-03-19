package emails;

import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.search.MessageIDTerm;

public class Delete {
    
	/*public static void deleteMessage(String user, String password, Scanner scanner) {
	    Properties properties = new Properties();
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", "smtp-mail.outlook.com");
	    properties.put("mail.smtp.port", "587");
	    
	    try {
	        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
	                return new javax.mail.PasswordAuthentication(user, password);
	            }
	        });
	        
	        Store store = session.getStore("imaps");
	        store.connect("imap.outlook.com", user, password);
	        
	        Folder folder = store.getFolder("INBOX");
	        folder.open(Folder.READ_WRITE);
	        
	        System.out.print("Enter the message ID: ");
	        String messageID = scanner.nextLine();
	        
	        Message[] messages = folder.search(new MessageIDTerm(messageID));
	        if (messages.length == 0) {
	            System.out.println("No messages found with the specified ID.");
	            return;
	        }
	        
	        for (Message message : messages) {
	            message.setFlag(Flag.DELETED, true);
	            System.out.println("Message deleted successfully.");
	        }
	        
	        folder.close(true);
	        store.close();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }*/
	public static void deleteMessage(String user, String password) {
	    Properties properties = new Properties();
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", "smtp-mail.outlook.com");
	    properties.put("mail.smtp.port", "587");
	    
	    try {
	        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
	                return new javax.mail.PasswordAuthentication(user, password);
	            }
	        });
	        
	        Store store = session.getStore("imaps");
	        store.connect("imap.outlook.com", user, password);
	        
	        Folder folder = store.getFolder("INBOX");
	        folder.open(Folder.READ_WRITE);
	        Scanner myID = new Scanner(System.in);
	        System.out.print("Enter the message ID: ");
	        String messageID = myID.nextLine();
	        
            /*System.out.println("Do you want to reply to only the sender or everyone? (Enter 1 for Sender, 2 for Everyone)");
            int replyType = scanner.nextInt();*/
	        
	        Message[] messages = folder.search(new MessageIDTerm(messageID));
	        if (messages.length == 0) {
	            System.out.println("No messages found with the specified ID.");
	            return;
	        }
	        
	        for (Message message : messages) {
	            message.setFlag(Flag.DELETED, true);
	            System.out.println("Message deleted successfully.");
	        }
	        
	        folder.close(true);
	        store.close();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }}
}


