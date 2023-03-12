package com.example.projetcrypto.ibescheme;

import java.io.Serializable;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


public class PublicParameter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Element generator;
	static Element publicKey;
	
	//chargement des parametres de configuration de la courbes elliptiques
	public static PairingParameters pairingParameters = PairingFactory.getPairingParameters("./src/main/resources/params/curves/a.properties");
    public static Pairing pairing = PairingFactory.getPairing(pairingParameters);
	
    // chargement des groupes cycliques Zr et G1
    @SuppressWarnings("rawtypes")
	public static Field fieldZr= pairing.getZr();
	@SuppressWarnings("rawtypes")
	public static Field fieldG1= pairing.getG1();

	
	public void set_up(Element secretMasterKey) {
		//choix aleatoire du generateur du groupe 
		generator = fieldG1.newRandomElement();
		//edition de la clé publique de l'autorité
		publicKey = generator.duplicate().mulZn(secretMasterKey);
	}
	
	//definition des fonctions de hachages
	public Element hash1(byte[] id) {
		/**
		 * correspond a la premiere fonction de hachage H1
		 * selon le schema cryptographique IBE de Boneh et Franklin
		 *  
		 * @param id tableau de bytes
		 * @return l'equivalent dans le groupe cyclique G1
		 */
		
		return fieldG1.newElement().setFromHash(id, 0, id.length);	
	}
		
	public byte[] hash2(Element gt) {
		/**
		 * correspond a la deuxieme fonction de hachage H2
		 * selon le schema cryptographique IBE de Boneh et Franklin
		 * 
		 * @param gt element du groupe cyclique GT
		 * @return l'equivalent en tableau de bytes
		 */
		return gt.toBytes();
	}
		
	
}

