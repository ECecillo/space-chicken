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
  - `GET /authenticate`
  - `POST /logout`
  - `GET /users/{login}`

**Aide :** Pour autoriser les scripts d'un client CORS à récupérer le header "Authorization" qui contient le token JWT, vous devrez ajouter un header [Access-Control-Expose-Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Expose-Headers) aux réponses avec succès aux POST sur '/login'.

Ressources :
- [Tutoriel](https://spring.io/guides/gs/rest-service-cors/) (trop) complet
- [Billet de blog](https://spring.io/blog/2015/06/08/cors-support-in-spring-framework) limité aux annotations, mais pas à jour

Pour tester, vous pouvez utiliser un client installé sur votre machine (par exemple, Postman ou Insomnia), sur lequel vous aurez importé le fichier OpenAPI généré par Springdoc

<!-- [cette page](test-cors.html), que vous pouvez placer sur un serveur installé sur le port 80 de votre machine -->

## Instructions de rendu

Les TPs 1 et 2 sont à rendre pour le **dimanche 5 mars 2023 à 23h59** (nouvelle date).

Votre dépôt de code doit comporter les éléments suivants :
- un répertoire `users` à la racine, contenant le code de la partie Spring Boot
- un fichier `users-api.yaml`, également à la racine du dépôt, contenant la documentation OpenApi générée
- un fichier, toujours à la racine, contenant une collection Postman permettant de tester les requêtes (avec ou sans CORS) sur votre serveur
- un fichier readme avec une section "TP1 & TP2", contenant les liens vers :
  - le fichier YAML sur le dépôt
  - le Swagger généré par Spring et déployé sur votre VM (et qui doit permettre de tester votre serveur)

## 4. Préparation des TPs suivants

Une fois que vous aurez déployé le war de votre projet Spring sur le Tomcat de votre VM, vous pouvez commencer à préparer l'infrastructure pour le serveur de l'application que vous allez réaliser aux prochains TPs. Pour cela :

- Initialisez un projet NPM nommé "game-express" (node et NPM sont déjà installés) : `npm init`
- Ajoutez une dépendance vers le serveur Express : `npm install express`
- Faites tourner l'application de base située [ici](https://expressjs.com/fr/starter/hello-world.html) sur le port 3000
- Configurez nginx en proxy HTTPS pour Express sur l'URL : https://192.168.75.xxx/game/ et testez que vous pouvez requêter l'URL avec votre navigateur
- Créez un nouveau service qui permet de (re)lancer cette application...
