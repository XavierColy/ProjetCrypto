package com.example.projetcrypto.mail;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.projetcrypto.ibescheme.PublicParameter;

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

// classe pour stocke pour le resultat apres chiffrement Ibe
final class EncryptResult {
	
	public byte[] element2cyphertext;
	public Element element1cyphertext;
	
	/**
	 * 
	 * @param element1cyphertext Équivalent du premier élement du couple resultat du chiffrement 
	 * @param element2cyphertext Équivalent du second élement du couple resultat du chiffrement
	 * 
	 * @note: couple resultat du chiffrement (rP, M xor elementGt) == cyphertext ; voire la methode encryptMessage
	 * 
	 * 
	 */
	public EncryptResult(Element element1cyphertext,byte[] element2cyphertext){
		this.element2cyphertext = element2cyphertext;
		this.element1cyphertext = element1cyphertext;
	}
	
}
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
	
	
	/**
	 * 
	 * @param message Message à chiffrer
	 * @param id Identité du destinataire
	 * @param publicKey Clé publique du système
	 * @param generator Générateur de groupe cyclique G
	 * 
	 * @return un couple de resultat du message chiffré
	 */
	public static EncryptResult encrypt(byte[] message,byte[] id,Pairing pairing,Element publicKey,Element generator) {
	
		
		Element Qid = PublicParameter.hash1(id, pairing);// Qid
		
		Element r = pairing.getZr().newRandomElement();
		Element result1 = generator.duplicate().mulZn(r);// element rP
		
		//couplage de Qid et Ppub --> element GT
		Element elementGt = pairing.pairing(Qid,publicKey);
		Element parametreH2 = elementGt.duplicate().powZn(r);
		
		byte[] result_H2 = PublicParameter.hash2(parametreH2);
		
		// !! taille des elements ???
		int taille = result_H2.length;
		byte[] resizedMessage = Arrays.copyOf(message,taille);// redimensionne la taille du message afin de faire le xor
		
		return new EncryptResult(result1,xor(resizedMessage,result_H2,taille));
		
	}
	
	/**
	 * 
	 * @param a 
	 * @param b
	 * @param taille de a et b (meme taille ?!!)
	 * @return Le resulstat du xor entre a et b
	 */
	public static byte[] xor(byte[] a , byte[] b,int taille) {
		byte[] result = new byte[taille];
		for( int i=0;i<taille;i++) {
			result[i] = (byte) (a[i] ^ b[i]);
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param c C'est le couple constituant le cyphertext
	 * @param pairing Objet contenant une configuration de courbe
	 * @param secretKey clé secrète de déchiffrement
	 * 
	 * @return le message initialement chiffré
	 */
	public static byte[] decrypt(EncryptResult c,Pairing pairing, Element secretKey) {
		Element couplage = pairing.pairing(secretKey,c.element1cyphertext);
		byte[] resultH2 = PublicParameter.hash2(couplage);
		int taille =resultH2.length;
		//System.out.println(taille);
		return xor(c.element2cyphertext,resultH2,taille);
	}
	
	
	public static void main(String[] arg){
        
		//test
		
		/*String url = "http://10.4.12.136:8080/service";
		String user = "koffi";
		@SuppressWarnings("unused")
		Client a = new Client(user,receptionConfig(user,url));
		*/
	}
}
