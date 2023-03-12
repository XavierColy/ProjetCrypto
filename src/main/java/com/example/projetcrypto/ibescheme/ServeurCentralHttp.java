package com.example.projetcrypto.ibescheme;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class ServeurCentralHttp {
	  
	public static PairingParameters pairingParameters = PairingFactory.getPairingParameters("./src/main/resources/params/curves/a.properties");
    public static Pairing pairing = PairingFactory.getPairing(pairingParameters);
	
    // chargement du groupe cyclique Zr
    @SuppressWarnings("rawtypes")
	public static Field fieldZr= pairing.getZr();
    // chargement du groupe cyclique G1
 	@SuppressWarnings("rawtypes")
 	public static Field fieldG1= pairing.getG1();
 	
 	private static Element secretMasterKey;

	
	public void set_up() {
		/**
		 * utlise secretMasterKey : cle secrete de l'autorité de confiance
		 * 
		 * initialise generator et publicKey
		 */
		//choix aleatoire du generateur du groupe
		Element elementG1 = fieldG1.newRandomElement();
		PublicParameter.generator = elementG1;
		//edition de la clé publique de l'autorité
		PublicParameter.publicKey = elementG1.duplicate().mulZn(secretMasterKey);
	}
	
	public Element key_Gen(byte[] id) {
		/**
		 * @param id identité convertit tableau de byte
		 * 
		 * @return cle secrete utilisateur
		 */
		Element Qid = PublicParameter.hash1(id);
		return Qid.duplicate().mulZn(secretMasterKey);
	}
	
	  

}
