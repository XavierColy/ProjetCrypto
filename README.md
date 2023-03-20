## Processus chiffrement, déchiffrement de pièce jointe



    alpha ---> [example]
    
                resultat

où **example** est une fonction, **alpha** le paramètre principale à son entrée et sa sortie est **resultat**.


Dans le sens de l'envoi de message :

      Fichier initiale ---> [divide]

                          ArrayList<byte[]> ---> [Encrypt]
                              
                                                EncryptResult ---> [produireFichierChiffrer]
                                                               
                                                                   fichier à envoyer par mail.
                                                                                                       
                                                                                                   
Dans le sens inverse :

      fichier recu par mail ---> [fileToObject]
                                          
                                  EncryptResult ---> [decrypt]
                                            
                                                   ArrayList<byte[]> ---> [produireFichierDechiffrer]
                                                              
                                                                                fichier initiale.
                                                                                      
Consignes:

* le resulat de [produireFichierChiffrer] est d'extension .encrypt (en plus de l'extension initiale du fichier)
* le fichier recu par mail est stocker comme temporaire 
* Une fois le fichier recu, verifier si il contient l'extension .encrypt, le cas échéant : retirer l'extension et procéder à la suite. Sinon rendre le fichier definitif.
* Une fois le fichier initilale retrouvée, le sauvegarder en definitif , supprimer le fichier temporaire.
