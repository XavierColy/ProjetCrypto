package com.example.projetcrypto.ibescheme;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.projetcrypto.mail.ConfigClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * Cette classe contient les parametres publiques comme decrit dans le schema IBE de Boneh et Franklin  
 * 
 * @param pairing Configuration de la courbe elliptique
 * @param generator Generateur d'un groupe cyclique
 * @param publicKey Clé publique du maitre, l'authorité de confiance
 * 
 * @author Giovanni
 * 
 */


public class ServeurCentralHttp {
	 
	public static PairingParameters pairingParameters = PairingFactory.getPairingParameters("./src/main/resources/params/curves/a.properties");
    public static Pairing pairing = PairingFactory.getPairing(pairingParameters);
    @SuppressWarnings("rawtypes")
	public static Field fieldZr= pairing.getZr();
    // chargement du groupe cyclique G1
 	@SuppressWarnings("rawtypes")
 	public static Field fieldG1= pairing.getG1();
 	
 	private static Element secretMasterKey;
 	
 	
	
	/**
	 * Cette fonction initialise les paramètres publiques du système cryptographique: @Element generator et @Element publicKey
	 * 
	 * @param s La secretMasterKey, clé secrète de l'autorité de confiance
	 * 
	 * @return Une instance de la classe <code>PublicParameter<code> initialisée
	 */
	public static PublicParameter set_up() {
		
		secretMasterKey = fieldZr.newRandomElement();
		//choix aleatoire du generateur du groupe
		Element P = fieldG1.newRandomElement();
		//edition de la clé publique de l'autorité
		Element Ppub = P.duplicate().mulZn(secretMasterKey);
		
		return new PublicParameter(pairingParameters,P,Ppub);
	}
	
	
	
	
	/**
	 * Cette fonction génère la clé de chiffrement pour l'utilisateur
	 * 
	 * @param id identité convertit tableau de byte
	 * 
	 * @return clé secrète utilisateur
	 */
	public static Element key_Gen(byte[] id) {
		
		Element Qid = PublicParameter.hash1(id,pairing);
		
		return Qid.duplicate().mulZn(secretMasterKey);
	}
	
	
	public static void main(String[] args){
		
		/*
		 * !!!initiliser la graine du PRNG
		 */
		PublicParameter PP = set_up();
		
		try {
			
			System.out.println("my address:"+InetAddress.getLocalHost());
            InetSocketAddress s = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
            
            HttpServer server = HttpServer.create(s, 1000);
		    System.out.println(server.getAddress());
		    server.createContext("/service", new HttpHandler() {
		    	
		    	public void handle(HttpExchange he) throws IOException {

		    		byte[] bytes1 = new byte[Integer.parseInt(he.getRequestHeaders().getFirst("Content-length"))];
		            he.getRequestBody().read(bytes1);
		            String username = new String(bytes1);
		            System.out.println("message reçu " + username);
		            he.getRequestBody().close();
		            
		            Element secretKeyId = key_Gen(bytes1);
		            System.out.println(secretKeyId);
		            ConfigClient configClient = new ConfigClient(PP, secretKeyId);
		            
		            he.sendResponseHeaders(200, 0);
		            ObjectOutputStream out = new ObjectOutputStream(he.getResponseBody());
		            out.writeObject((Object)configClient);
		            //out.flush();
		            System.out.println("message envoyé au client");
		            out.close();
					
				}
		    });
		    server.start();

		} catch (IOException ex) {
			Logger.getLogger(ServeurCentralHttp.class.getName()).log(Level.SEVERE, null, ex);	
		} 
	}
	
	

}
