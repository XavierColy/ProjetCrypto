package com.example.projetcrypto.mail;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.projetcrypto.ibescheme.EncryptResult;
import com.example.projetcrypto.ibescheme.PublicParameter;
import com.example.projetcrypto.ibescheme.UtilsForEncryptSystem;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
/**
 * Implemente un client qui se connectera a un serveur via une url <code>ServeurCentralHttp<code>
 * Implemente les méthodes receptionConfig , encryptMessage et decryptMessage
 * 
 * @param username Identité du client
 * @param configClient <code>ConfigClient<code>
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

	
	/**
	 * 
	 * @param username Identité du client
	 * @param urlString url de connexion
	 * 
	 * @return la configuration du client pour chiffrer et déchiffrer le message
	 * 
	 */
	
	public static ConfigClient receptionConfig(String username, String urlString) {
		
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
	
	
	public void chiffrerPieceJointe(String path, String user,PublicParameter PP) {
		
		ArrayList<byte[]> byteListe = UtilsForEncryptSystem.divide(path,128);
		byte[] identity = user.getBytes();
		//
		Pairing pairing = PairingFactory.getPairing(PP.getPairingParameters());
		Element publicKey = pairing.getG1().newElementFromBytes(PP.getPublicKey());
		Element generator = pairing.getG1().newElementFromBytes(PP.getGenerator());
		//
		EncryptResult encrytResult = UtilsForEncryptSystem.encrypt(byteListe, identity, pairing, publicKey, generator);
		try {
			UtilsForEncryptSystem.produireFichierChiffrer(path, encrytResult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void dechiffrerPieceJointe(String path,Pairing pairing, Element secretKey) {
		
		EncryptResult encrytResult = UtilsForEncryptSystem.fileToObject(path);
		ArrayList<byte[]> byteListe = UtilsForEncryptSystem.decrypt(encrytResult, pairing, secretKey);
		try {
			UtilsForEncryptSystem.produireFichierDechiffrer(path, byteListe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] arg) throws Throwable{
		
		/*String url = "http://10.8.20.30:8080/service";
		String user = "koffi";
		@SuppressWarnings("unused")
		Client a = new Client(user,receptionConfig(user,url));
		
	    Pairing pairing = PairingFactory.getPairing(a.configClient.getPP().getPairingParameters());
	    
	    EncryptResult alpha = encrypt(divide("testfile/test.zip",128),user.getBytes(), pairing,pairing.getG1().newElementFromBytes(a.configClient.getPP().getPublicKey()),pairing.getG1().newElementFromBytes(a.configClient.getPP().getGenerator()));
	    ArrayList<byte[]> beta = decrypt(alpha, pairing,pairing.getG1().newElementFromBytes(a.configClient.getSecretKeyUid()));
	    
	    produireFichierDechiffrer("a.zip", beta);*/
	    
	}
}
