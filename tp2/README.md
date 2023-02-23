# TP Spring Boot plugins

Dans ce TP, vous allez approfondir vos connaissances sur Spring Boot. Vous continuerez la mise en place du serveur de gestion d'utilisateurs et d'authentification commencé au serveur précédent. Le métier de ce serveur ne va pas changer, mais vous allez l'enrichir en prenant en compte des préoccupations non métier, mais néanmoins incontournables : documentation, test, requêtage cross-domain.

## Objectifs pédagogiques

- Générer automatiquement une documentation d'API à l'aide d'annotations OpenAPI
- Créer des tests unitaires et d'intégration pour une API
- Comprendre et configurer les mécanismes CORS

## 1. Génération de la documentation

&Agrave; l'aide d'annotations OpenAPI, générez une documentation de l'API du serveur.

Ressources :
- [Page d'accueil de SpringDoc](https://springdoc.org/v2/)
- [Spécifications / Getting started](https://github.com/springdoc/springdoc-openapi)
- [Exemple de tutoriel assez complet](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)
- [Javadoc](http://docs.swagger.io/swagger-core/v2.1.1/apidocs/)

**Indications** :
- Attention, il faut indiquer la dernière version de springdoc-openapi dans votre pom.xml : **2.0.2**
- Pour "supprimer" le type de contenu de la documentation d'une erreur HTTP, il faut rajouter explicitement une annotation `@Content()` vide
- Si vous utilisez plusieurs fois la même annotation, seule la dernière sera prise en compte. En conséquence, pour pouvoir documenter deux méthodes répondant à la même URL (et par exemple générant différents types de contenus), il faut mettre toute la documentation dans une seule annotation.
- Voir [cette FAQ](https://springdoc.org/#can-i-customize-openapi-object-programmatically) pour grouper plusieurs contrôleurs dans la même documentation

## 2. Tests des contrôleurs MVC

Spring MVC propose un outil de test assez simple à utiliser : [MockMVC](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework). Vous trouverez [ici](https://spring.io/guides/gs/testing-web/) un tuto pour démarrer, et [là](https://www.baeldung.com/integration-testing-in-spring) des exemples pour aller plus loin.

- Créez des tests unitaires de vos contrôleurs en utilisant Spring MockMVC.
- Créez un test d'intégration en scénarisant (i.e. en enchaînant) vos tests unitaires<br>
Remarque : vous aurez peut-être besoin de l'annotation [@Order](https://junit.org/junit5/docs/5.4.0-RC1/api/org/junit/jupiter/api/Order.html)
- Documentez vos tests

## 3. Mise en place de CORS

Utilisez les annotations fournies par le [CORS support](https://jira.spring.io/browse/SPR-9278?redirect=false) de Spring pour supporter CORS de la façon suivante :
- Autoriser les origines `http://localhost`, `http://192.168.75.XX`, et `https://192.168.75.XX`, où XX est la fin de l'IP de votre VM, pour les requêtes :
  - `POST /login`
  - `POST /logout`
  - `GET /users/{login}`

**Aide :** Pour autoriser les scripts d'un client CORS à récupérer le header "Authorization" qui contient le token JWT, vous devrez ajouter un header [Access-Control-Expose-Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Expose-Headers) aux réponses avec succès aux POST sur '/login'.

Ressources :
- [Tutoriel](https://spring.io/guides/gs/rest-service-cors/) (trop) complet
- [Billet de blog](https://spring.io/blog/2015/06/08/cors-support-in-spring-framework) limité aux annotations, mais pas à jour

Pour tester, vous pouvez utiliser un client installé sur votre machine (par exemple, Postman ou Insomnia), sur lequel vous aurez importé le fichier OpenAPI généré par Springdoc

## Instructions de rendu

Les TPs 1 et 2 sont à rendre pour le **dimanche 5 mars 2023 à 23h59**.

Votre dépôt de code doit comporter les éléments suivants :
- un répertoire `users` à la racine, contenant le code de la partie Spring Boot
- un fichier `users-api.yaml`, également à la racine du dépôt, contenant la documentation OpenApi générée
- un fichier, toujours à la racine, contenant une collection Postman permettant de tester les requêtes (avec ou sans CORS) sur votre serveur
- un fichier readme avec une section "TP1 & TP2", contenant les liens vers :
  - le fichier YAML sur le dépôt
  - le Swagger généré par Spring et déployé sur votre VM (et qui doit permettre de tester votre serveur)

## 4. Préparation des TPs suivants

Une VM vous a été attribuée. Vous trouverez les informations de connexion sur Tomuss, et le matériel cryptographique dans le sous-répertoire M1IF13 puis Certificats de ma page "enseignements". Pour vous faire gagner du temps, elle a été un peu mieux configurée que celle de M1IF03. Voici les opérations de configuration à réaliser :

### 4.1. Récupération des certificats

- Récupérez les 3 fichiers -- clé, certificat et chaîne de certification -- concernant votre VM et identifiés par l'id de votre binôme dans Tomuss.
- Placez ces fichiers dans le répertoire `/etc/ssl/certs` en les nommant respectivement `server.key`, `server.cert` et `server-chain.cert`.
- Dans ce dossier, faites un `chmod 400 server.key` pour que les autres utilisateurs ne puissent pas voir la clé de votre serveur.

### 4.2. Configuration de nginx

- Vous allez tout d'abord passer nginx en HTTPS. Pour cela :
  - Vous trouverez dans `/etc/nginx/sites-available` un fichier `default-https` qui contient un début de configuration (non testée) mais qui devrait fonctionner directement une fois le matériel cryptographique accessible.
  - Rappel : les fichiers de ce répertoire ne sont pas activés mais uniquement disponibles ; vous devez créer un lien symbolique pour les activer (voir [M1IF03 TP2 1.2.2](https://perso.liris.cnrs.fr/lionel.medini/enseignement/M1IF03/#md=TP/md/TP2&p=122-mise-en-place-dun-nouveau-site))
  - Redémarrez le service nginx pour vérifier. Si vous avez toujours le certificat racine dans votre navigateur, https://192.168.75.XXX devrait être accessible directement
- Une redirection temporaire vers Tomcat devrait fonctionner également : https://192.168.75.XXX/api/ <br>
**Attention : cette redirection ne doit pas être conservée pour les TPs suivants.** Elle est simplement là pour vous rappeler comment faire et la prendre pour exemple pour la partie 4.4...
- Comme en M1IF03, modifiez également la config `default` pour forcer la redirection (code 308) depuis HTTP vers HTTPS. Redémarrez le serveur et vérifiez.

### 4.3. Configuration de Tomcat

Tomcat est pré-configuré pour vous faire gagner du temps. Vous pouvez notamment accéder à l'application manager avec les credentials que vous trouverez dans `/opt/tomcat/conf/tomcat-users.xml`.

Il est aussi configuré pour fonctionner en HTTPS sur le port 8443, avec un certificat auto-signé (votre navigateur va crier, mais si vous lui dites d'accepter ce certificat, vous pourrez accéder au site).

Pour que vous n'ayez plus ce problème, vous allez configurer Tomcat avec le même certificat que pour nginx (il a été attribué à votre nom de serveur, c'est-à-dire 192.168.75.XXX, quel que soit le port du serveur). Tomcat place ses certificats dans un "keystore". Nous en avons créé un qui contient le certificat auto-généré qui vous est fourni, et qui se trouve dans le fichier `/opt/tomcat/conf/.keystore`. Pour remplacer ce certificat généré "à la mode Java" par un certificat "propre" généré avec `openssl`, il y a plusieurs manipulations à réaliser (voir la [page de configuration SSL/TLS de Tomcat](https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html)). La plupart ont été réalisées pour vous. Il vous reste à :

- sauvegarder l'ancien keystore (au cas où) : `sudo mv /opt/tomcat/conf/.keystore /opt/tomcat/conf/.keystore.backup`
- utiliser openssl pour générer un nouveau keystore à partir du matériel cryptographique spécifique à votre VM :<br>
`sudo /usr/local/ssl/bin/openssl pkcs12 -export -in /etc/ssl/certs/server.cert -inkey /etc/ssl/certs/server.key -out ./.keystore -name 192.18.75.XXX -CAfile /etc/ssl/certs/server-chain.cert -caname m1if-ca -chain`
- modifier les propriétaires du nouveau fichier pour que Tomcat puisse y accéder : `sudo chown tomcat:tomcat .keystore`
- redémarrer le service tomcat

Cela fait, testez (à l'aide de l'application manager) le déploiement du war de votre projet Spring et vérifiez que vous y accédez à l'URL : https://192.168.75.XXX:8443/users/

### 4.4. Premier contact avec NodeJS et Express

Node et NPM sont pré-installés sur votre VM. Si vous savez à peu près à quoi cela correspond, vous pouvez commencer à préparer l'infrastructure pour le serveur de l'application que vous allez réaliser aux prochains TPs. Pour cela :

- Initialisez un projet NPM nommé "game-express" (node et NPM sont déjà installés) : `npm init`
- Ajoutez une dépendance vers le serveur Express : `npm install express`
- Faites tourner l'application de base située [ici](https://expressjs.com/fr/starter/hello-world.html) sur le port 3376
- Configurez nginx en proxy HTTPS pour Express (en vous inspirant fortement de la config existante) sur l'URL : https://192.168.75.xxx/game/ et testez que vous pouvez requêter l'URL avec votre navigateur
