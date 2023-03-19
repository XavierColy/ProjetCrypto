package com.example.projetcrypto.mail;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.example.projetcrypto.ibescheme.PublicParameter;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
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
	
	public ArrayList<byte[]> element2cyphertext;
	public Element element1cyphertext;
	
	/**
	 * 
	 * @param element1cyphertext Équivalent du premier élement du couple resultat du chiffrement 
	 * @param element2cyphertext Équivalent du second élement du couple resultat du chiffrement disposer dans une liste
	 * 
	 * @note: couple resultat du chiffrement (rP, M xor elementGt) == cyphertext ; voire la methode encryptMessage
	 * 
	 * 
	 */
	public EncryptResult(Element element1cyphertext,ArrayList<byte[]> element2cyphertext){
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
	public static EncryptResult encrypt(ArrayList<byte[]> message,byte[] id,Pairing pairing,Element publicKey,Element generator) {
	
		
		Element Qid = PublicParameter.hash1(id, pairing);// Qid
		
		Element r = pairing.getZr().newRandomElement();
		Element result1 = generator.duplicate().mulZn(r);// element rP
		
		//couplage de Qid et Ppub --> element GT
		Element elementGt = pairing.pairing(Qid,publicKey);
		Element parametreH2 = elementGt.duplicate().powZn(r);
		
		byte[] result_H2 = PublicParameter.hash2(parametreH2);
		
		// !! taille des elements ???
		int taille = result_H2.length;
		ArrayList <byte[]> result2 = new ArrayList<byte[]>();
		
		for(byte[] messageOfPart : message) {
			
			byte[] resizedMessage = Arrays.copyOf(messageOfPart ,taille);// redimensionne la taille du message afin de faire le xor
			result2.add(xor(resizedMessage,result_H2,taille));
		}
		
		return new EncryptResult(result1,result2);
		
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
	 * @return le message initialement chiffré disposer dans une liste
	 */
	public static ArrayList<byte[]> decrypt(EncryptResult c,Pairing pairing, Element secretKey) {
		Element couplage = pairing.pairing(secretKey,c.element1cyphertext);
		byte[] resultH2 = PublicParameter.hash2(couplage);
		int taille =resultH2.length;
	
		ArrayList <byte[]> resultat  = new ArrayList<byte[]>();
		
		for(byte[] part : c.element2cyphertext) {
			resultat.add(xor(part,resultH2,taille));
		}
		return resultat ;
	}
	
	
	/**
	 * 
	 * Cette fonction transforme une instance de <code>EncryptResult<code> en un fichier qui sera envoyer
	 * 
	 * @param path chemin du fichier de sortie
	 * @param entree Objet <code>EncryptResult<code>
	 * @throws IOException
	 */
	public static void produireFichierChiffrer(String path,EncryptResult entree) throws IOException {
		
		 FileOutputStream fileOutputStream = new FileOutputStream(path+".encrypt");
         // Créer un flux de sérialisation pour sérialiser l'objet dans le flux de sortie
         ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
         out.writeObject(entree);
         // Fermer les flux
         out.close();
         fileOutputStream.close(); 
	}
	
	
	/**
	 * 
	 * @param le chemin et le nom du fichier
	 * @return l'extension d'un fichier
	 */
	public static String getFileExtension(String filename) {
	    String [] liste = filename.split("\\.");
	    return liste[liste.length-1];
	}
	
	
	/**
	 * Cette fonction transforme la liste de tableau de byte en un fichier final de sortie @param path
	 * 
	 * @param path Chemin et fichier de sortie
	 * @param liste resultat du message dechiffré
	 * @throws IOException
	 */
	public static void produireFichierDechiffrer(String path, ArrayList<byte[]> liste) throws IOException {
		// Déterminer la taille totale du tableau de bytes nécessaire
        int tailleTotale = 0;
        for (byte[] element : liste) {
            tailleTotale += element.length;
        }

        // Créer le tableau de bytes de la taille nécessaire
        byte[] tableauBytes = new byte[tailleTotale];

        // Copier les éléments de la liste dans le tableau de bytes
        int position = 0;
        for (byte[] element : liste) {
            System.arraycopy(element, 0, tableauBytes, position, element.length);
            position += element.length;
        }
        String extension = getFileExtension(path);
        
        if(extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg")) {
        	BufferedImage image = ImageIO.read(new ByteArrayInputStream(tableauBytes));
        	// Écriture de l'image dans un fichier (facultatif)
        	File output = new File(path);
        	ImageIO.write(image, "jpg", output);
        }
        else {
        	if(extension.equals("pdf")) {
        		;
        	}
        	else{
        		// Conversion des bytes en une chaîne de caractères
        		String texte = new String(tableauBytes, StandardCharsets.UTF_8);

        		// Écriture de la chaîne de caractères dans un fichier
        		File output = new File(path);
        		Files.write(output.toPath(), Collections.singleton(texte), StandardCharsets.UTF_8);
        	}
        }
      
	}
	
	
	
	/**
	 * 
	 * Permet de convertir un fichier en liste de tableau de bytes.
	 * 
	 * @param piece chemin vers fichier
	 * @param size taille maximal(par defaut) des elements de la liste
	 *  
	 * @return une liste de tableau de bytes.
	 * 
	 * @throws Throwable
	 */
	public static ArrayList<byte[]> divide(String piece,int size) throws Throwable{
		
		FileInputStream fis = new FileInputStream(piece);
	    int sz=fis.available();
	    byte[] b=new byte[sz];
	    fis.read(b);
	    int o = sz%size;	//know how much bytes under 128
	    ArrayList<byte[]> q = new ArrayList<byte[]>();//we add the byte array to an arraylist

	    for (int i = 0; i < b.length; i += size) {
	        q.add( Arrays.copyOfRange(b, i, Math.min(i + size, b.length)));
	    }
	    q.add( Arrays.copyOfRange(b, sz-o,sz));

	    return q;

	}
	
	
	public static EncryptResult fileToObject(String path) {
		
		EncryptResult encryptResult = null;
		
		try {
	        FileInputStream fileInputStream = new FileInputStream(path);
	        // Création de l'instance ObjectInputStream pour lire l'objet sérialisé
	        ObjectInputStream out = new ObjectInputStream(fileInputStream);
	
	        // Lecture de l'objet sérialisé et conversion en objet de classe Java
	        Object objetDeserialise = out.readObject();
	        out.close();
	        fileInputStream.close();
	
	        // Conversion explicite de l'objet en instance de la classe Java appropriée
	        encryptResult = (EncryptResult) objetDeserialise;
	        
		} catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
			
		return encryptResult ;
	}
	
	
	
	public static void main(String[] arg) throws Throwable{
		
		String url = "http://10.192.62.89:8080/service";
		String user = "koffi";
		@SuppressWarnings("unused")
		Client a = new Client(user,receptionConfig(user,url));
		
	    Pairing pairing = PairingFactory.getPairing(a.configClient.getPP().getPairingParameters());
	    
	    EncryptResult alpha = encrypt(divide("image65.jpg",128),user.getBytes(), pairing,pairing.getG1().newElementFromBytes(a.configClient.getPP().getPublicKey()),pairing.getG1().newElementFromBytes(a.configClient.getPP().getGenerator()));
	    ArrayList<byte[]> beta = decrypt(alpha, pairing,pairing.getG1().newElementFromBytes(a.configClient.getSecretKeyUid()));
	    
	    produireFichierDechiffrer("text3.jpg", beta);
	    
	}
}
