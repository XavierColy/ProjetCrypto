package com.example.projetcrypto.mail;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
/**
 * 
 * @author Giovanni
 *
 */
public class Client {
    
	private ConfigClient configClient;
	private String username;
	
	
	public Client(String username, ConfigClient configClient ) {
		super();
		this.username = username;
		this.configClient = configClient;
	}

	//getter, setter
	public ConfigClient getConfigClient() {
		return configClient;
	}
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private static ConfigClient receptionConfig(String username, String urlString) {
		
		ConfigClient configUser = null;
		try {
        	
        	URL url = new URL(urlString);
            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            System.out.println("connexion établie...");
            OutputStream out = urlConn.getOutputStream();
            out.write(username.getBytes());
            out.close();
            //
            Object updatedObject = new Object();
			ObjectInputStream in = new ObjectInputStream(urlConn.getInputStream());
			if ( in!= null && (in.available()==0)) {
				// Traiter les données lues à partir du flux
				try {
					updatedObject = in.readObject();
				} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
							
			} else {
				 // Le flux est fermé
				System.out.println("impossible de lire le flux");
		   
			}
	        in.close();
	        //recuperation de l'objet
	        configUser = (ConfigClient) updatedObject;
	        byte[] result = configUser.getSecretKeyUid();
	        System.out.println("Objet modifié reçu: "+ PairingFactory.getPairing(configUser.getPP().getPairingParameters()).getG1().newElementFromBytes(result));
	        
        } catch (MalformedURLException ex) {
        	Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("fin des échanges avec le serveur");
		
        return configUser;
		
	}
	
	public static void main(String[] arg){
        
		//test
		String url = "http://10.4.12.136:8080/service";
		String user = "koffi";
		@SuppressWarnings("unused")
		Client a = new Client(user,receptionConfig(user,url));
		
    }  
}
