package com.example.projetcrypto.ibescheme;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import it.unisa.dia.gas.jpbc.Element;

public class EncryptResult implements Serializable {

    //classe pour stocke pour le resultat apres chiffrement Ibe
    @Serial
    private Long serialId = 1L;

    private ArrayList<byte[]> element2cyphertext;
    private byte[] element1cyphertext;

    public ArrayList<byte[]> getElement2cyphertext() {
        return element2cyphertext;
    }

    public void setElement2cyphertext(ArrayList<byte[]> element2cyphertext) {
        this.element2cyphertext = element2cyphertext;
    }

    public byte[] getElement1cyphertext() {
        return element1cyphertext;
    }

    /**
     * @param element1cyphertext Équivalent du premier élement du couple resultat du chiffrement
     * @param element2cyphertext Équivalent du second élement du couple resultat du chiffrement disposer dans une liste
     * @note: couple resultat du chiffrement (rP, M xor elementGt) == cyphertext ; voire la methode encryptMessage
     */
    public EncryptResult(Element element1cyphertext, ArrayList<byte[]> element2cyphertext) {
        this.element2cyphertext = element2cyphertext;
        this.element1cyphertext = element1cyphertext.toBytes();
    }
}

