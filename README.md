# Projet Crypto

## Description
L’objectif de ce projet est de réaliser un client mail permettant d’envoyer et de recevoir des mails contenant des pièces jointes chiffrées avec l’algorithme de chiffrement à base d’identité.

## Installation
Ajouter tous les jars se trouvant dans le dossier resources/jars (en excluant le dossier test) dans la structure du projet.
Sur Intellij, aller dans File -> Project structure -> Libraries -> New project library (bouton +) et ajoutez tous les jars

## Exécution
Il faut d'abord exécuter le fichier certificateAuthority/ServeurCentralHttp.java pour lancer le serveur puis récupérer l'adresse IP de celui-ci dans le terminal d'exécution. Ensuite, il faut ajouter 
cette adresse dans la variable url du fichier controllers/LoginController.java. Vous pouvez maintenant exécuter le fichier Main.java.


## Processus chiffrement, déchiffrement de pièce jointe(fichier texte et image)



    alpha ---> [example]
    
                resultat

où **example** est une fonction, avec **alpha** son paramètre principale en entrée, et la fonction **example** donne en sortie **resultat**.


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

* le resulat de *[produireFichierChiffrer]* est d'extension .encrypt (en plus de l'extension initiale du fichier)
* le fichier recu par mail est stocker comme temporaire (pour le moment est stocker)
* Une fois le fichier recu, verifier si il contient l'extension .encrypt, le cas échéant : retirer l'extension et procéder à la suite. Sinon rendre le fichier definitif.
* Une fois le fichier initilale retrouvée, le sauvegarder en definitif , supprimer le fichier temporaire.
