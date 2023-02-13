# Utilisation des certificats HTTPS pour Tomcat

Cette page indique comment faire en sorte que Tomcat réponde en HTTPS sur le port 8443 en utilisant le certificat signé par la racine du département.

Elle est largement basée sur [ce tutoriel](https://confluence.atlassian.com/kb/how-to-import-an-existing-ssl-certificate-for-use-in-tomcat-838412853.html) et sur [la doc de Tomcat](https://tomcat.apache.org/tomcat-9.0-doc/ssl-howto.html).

Globalement, vous allez de créer un _keystore_ qui va agréger les informations de votre certificat dans un format lisible par l'outil de gestion cryptographiques de Java [JSSE](https://docs.oracle.com/javase/9/security/java-secure-socket-extension-jsse-reference-guide.htm), puis de configurer Tomcat pour qu'il l'utilise. La création du keystore se fait en deux étapes.

## Prérequis

Vous avez récupéré et téléchargé sur votre VM le matériel cryptographique qui la concerne :

- certificat (_votre-VM-ca.cert_)
- chaîne de certification (_votre-VM-ca-chain.cert_)
- clé privée du certificat (_votre-VM.key_)

Dans la suite, j'utiliserai ces notations pour désigner **le chemin d'accès complet** à ces fichiers.

## Création d'un keystore à partir d'un certificat existant

Pour séparer proprement les fichiers créés de ceux de Tomcat, créez un répertoire `/opt/tomcat/ssl`, et utilisez les commandes `chown` et `chgrp` pour que Tomcat puisse les lire. Placez-vous dans ce répertoire.

### Création d'un keystore intermédiaire au format PKCS12

```
openssl pkcs12 -export -in votre-VM-ca.cert
                       -inkey votre-VM.key
                       -CAfile votre-VM-ca-chain.cert
                       -out keystore.p12
                       -name tomcat
                       -caname root-ca
                       -chain
```

Un mot de passe vous sera demandé. Conservez-le mais attention : vous devrez l'écrire en clair un peu plus loin -> appelons-le _mdp1_.

Cette opération vous crée un fichier `keystore.p12`.

### Création d'un keystore au format JKS (de Tomcat/JSSE)

```
keytool -importkeystore -deststorepass mdp1
                        -destkeypass mdp1
                        -destkeystore .keystore
                        -srckeystore keystore.p12
                        -srcstoretype PKCS12
                        -srcstorepass mdp1
                        -alias tomcat
```

Pour plus de simplicité, j'ai conservé le même mot de passe pour le second keystore mais vous pouvez vous amuser à le changer. Dans ce cas, il faudra utiliser le nouveau dans la prochaine étape...

Cette opération vous crée un fichier `.keystore`.

## Configuration de Tomcat

Modifiez le fichier `/opt/tomcat/conf/server.xml` en ajoutant l'élément suivant :

```xml
    <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
               maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
               keystoreFile="/opt/tomcat/ssl/.keystore" keystorePass="mdp1"
               clientAuth="false" sslProtocol="TLS"/>
```

N'oubliez pas de changer "mdp1" par votre mot de passe.

Redémarrez Tomcat et naviguez sur https://192.168.75.XX:8443/ Si vous avez préalablement accepté le certificat racine (cf. M1IF03 TP1), la page d'accueil de Tomcat devrait s'afficher en HTTPS sans warning.
