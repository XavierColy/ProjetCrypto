package emails;

import java.io.IOException;
import java.util.Scanner;
//import emails.SendEmail;
import emails.*;
public class main {

	public static void main(String[] args) throws IOException{
		
	    String user = "cryptotest10@outlook.com";
	    String password = "Azerty@2023";
	    String destination = "kaoutartanani27@gmail.com";
	    String replyMessage= "That's my reply msg";
	    Email.listMessageIDs(user, password);
	   // String attachmentPath = "C:/Documents/example.pdf";
	   // SendEmail.sendmessage(user, password, destination, object, subject);
	   //SendEmail.sendmessagewithattachement(user, password, destination, attachmentPath);
	   // SendEmail.downloadEmailAttachments(user, password);
	   forward.forwardMessage(user, password, destination);
	   //Delete.deleteMessage(user, password, randomMsgID);
	  
	  
	    //ReplyAll.replyToMessage(user, password,replyMessage);
	    
	    //Delete.deleteMessage(user, password);
	    
	}


}
