package com.example.projetcrypto.mail;

import java.io.Serializable;

import com.example.projetcrypto.ibescheme.PublicParameter;
import it.unisa.dia.gas.jpbc.Element;

/**
 * Classe dont les attributs sont la configuration de client pour le chiffrement et dechiffrement
 *  de message par la methode suivant le schema de IBE de Boneh et Franklin
 *
 * @param PP <code>PublicParameter<code>
 * @param secretKey clé secrète de chiffrement du client
 * 
 * 
 * @author Giovanni
 *
 */
public class ConfigClient implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PublicParameter PP;
	private byte[] secretKeyUid;
	
	
	public ConfigClient(PublicParameter PP,Element secretKey) {
		
		this.PP = PP;
		this.secretKeyUid = secretKey.toBytes();	
	}
    
	
 	//Getters
	public PublicParameter getPP() {
		return PP;
	}

	public byte[] getSecretKeyUid() {
		return secretKeyUid;
	}

	

}
