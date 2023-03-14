package com.example.projetcrypto.ibescheme;

import java.io.Serializable;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

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
public class PublicParameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] generator;
	private byte[] publicKey;
	private PairingParameters pairingParameter ;
	
	
	public PublicParameter(PairingParameters pairingParameters ,Element generator, Element publicKey) {

		this.pairingParameter = pairingParameters;
		this.generator = generator.toBytes();
		this.publicKey = publicKey.toBytes();
		
	}
	
	//getters, setters
	public byte[] getGenerator() {
		return generator;
	}

	public void setGenerator(Element generator) {
		this.generator = generator.toBytes();
	}

	public byte[]  getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Element publicKey) {
		this.publicKey = publicKey.toBytes();
	}
		
	public PairingParameters getPairingParameters() {
		return pairingParameter;
	}

	//definition des fonctions de hachages
	
	/**
	 * correspond a la premiere fonction de hachage H1
	 * selon le schema cryptographique IBE de Boneh et Franklin
	 * 
	 * @param id Tableau de bytes correspondant à une identité
	 * @param pairing
	 * @return L'equivalent de @param id dans le groupe cyclique G1
	 */
	public static Element hash1(byte[] id,Pairing pairing) {
		
		/*Element Qid = pairing.getG1().newOneElement();
		Qid.setFromHash(id, 0, id.length);
		
		return Qid;*/
		return pairing.getG1().newElementFromBytes(id);
	}
	
	/**
	 * correspond a la deuxieme fonction de hachage H2
	 * selon le schema cryptographique IBE de Boneh et Franklin
	 * 
	 * @param gt Element du groupe cyclique GT
	 * @return L'equivalent en tableau de bytes
	 */
	public static byte[] hash2(Element gt) {
		
		return gt.toBytes();
	}
		
	
}

