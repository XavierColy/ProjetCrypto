package com.example.projetcrypto.ibescheme;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;



	
public class UtilsForEncryptSystem {

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
		Element couplage = pairing.pairing(secretKey,pairing.getG1().newElementFromBytes(c.getElement1cyphertext()));
		byte[] resultH2 = PublicParameter.hash2(couplage);
		int taille =resultH2.length;
	
		ArrayList <byte[]> resultat  = new ArrayList<byte[]>();
		
		for(byte[] part : c.getElement2cyphertext()) {
			resultat.add(xor(part,resultH2,taille));
		}
		return resultat ;
	}
	
	
	
	public static String removeLastNchars(String str, int n) {
		return str.substring(0, str.length() - n);
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
	 * @param filename le chemin et le nom du fichier
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
        	if(extension.equals("txt")) {
        		//Conversion des bytes en une chaîne de caractères
        		String texte = new String(tableauBytes, StandardCharsets.UTF_8);
        		// Écriture de la chaîne de caractères dans un fichier
        		File output = new File(path);
        		Files.write(output.toPath(), Collections.singleton(texte), StandardCharsets.UTF_8);
        	}
        	else{
        		//for pdf, html, zip (only for folder),...
        		FileOutputStream fileOutputStream = new FileOutputStream(path);
        	    fileOutputStream.write(tableauBytes);
        	    fileOutputStream.close();
        	    System.out.println("File written successfully");

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
	public static ArrayList<byte[]> divide(String piece,int size) {
		
		ArrayList<byte[]> q = new ArrayList<byte[]>();//we add the byte array to an arraylist
		try (FileInputStream fileInputStream = new FileInputStream(piece)) {
			int taille = fileInputStream.available();
			byte[] b=new byte[taille];
			fileInputStream.read(b);
			int mod = taille%size;	//know how much bytes under 128
			

			for (int i = 0; i < b.length; i += size) {
			    q.add( Arrays.copyOfRange(b, i, Math.min(i + size, b.length)));
			}
			q.add( Arrays.copyOfRange(b, taille-mod,taille));

		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return q;

	}
	
	/**
	 * 
	 * @param path Chemin du fichier correspondant à l'objet serialisé
	 * 
	 * @return l'instance de l'objet <code>EncryptResult<code> correspondante au fichier dont le chemin est passé en paramètre
	 * 
	 */
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
	
	
	
	
	
	
	
	
}
