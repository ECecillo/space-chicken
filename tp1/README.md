# TP1 - Framework côté serveur

Dans ce TP, vous allez utiliser le framework Spring pour générer un serveur qui permettra de gérer les utilisateurs de votre application. Ce serveur sera entièrement séparé de celui qui gèrera le jeu.

Ce serveur aura 2 contrôleurs :

- un contrôleur REST qui permettra de gérer les utilisateurs (CRUD)
- un contrôleur d'opérations qui permettra :
  - aux utilisateurs enregistrés de se loguer et de se déloguer (JWT)
  - au serveur de gestion du jeu de vérifier le token d'un utilisateur

Vous pousserez le code correspondant à ce TP dans un répertoire `users` sur votre projet forge.

Pour tester, vous utiliserez une collection de requêtes Postman que vous élaborerez au fur et à mesure du TP, et que vous pousserez dans le même répertoire.

## Mise en place d'une application Spring Boot

### Initialisation du projet

Créez une nouvelle application Spring Boot à l'aide de https://start.spring.io/.

Conservez les réglages par défaut pour la version de Spring Boot et la version de Java. Modifiez les autres options comme suit :

- type de projet : Maven
- méthode de packaging : war
- métadonnées :
  - Group : fr.univlyon1.m1if.m1if13-2023
  - Artifact = Name : users

Ajoutez 2 dépendances : *Spring Web* et *Thymeleaf*.

Générez le projet et sauvegardez-le dans `users`.

#### Lancement avec Maven

Buildez, exécutez et testez cette application (*cf*. cours), à l'aide du goal Maven approprié.

Vous devez voir une page d'erreur sur le port 8080 de votre machine locale : c'est une bonne nouvelle !<br>Cela signifie que Spring est lancé et que le serveur est démarré mais qu'il n'existe pas de page d'accueil pour l'application.

Pour créer une page d'accueil statique, il faut la placer dans le répertoire `src/main/resources/static`.

#### Déploiement dans Tomcat

Dans l'élément `build` de votre `pom.xml`, rajoutez la ligne suivante :

`<finalName>${artifactId}</finalName>`

(cela vous permettra de générer un fichier war avec le nom "simple" de votre application : `users` et de déployer dans Tomcat avec ce nom de contexte).

&Agrave; l'aide du goal `mvn package`, créez un war de votre application. Vérifiez son existence dans le répertoire `target`. Déployez-le sur votre serveur Tomcat local et vérifiez que vous obtenez le même résultat que précédemment. 

## Conception de l'application

Pour vous aider à démarrer, nous vous fournissons la classe `User`, l'interface de son DAO ainsi que le début d'implémentations d'un contrôleur Spring.

### Création d'un bean Spring

Comme pour M1IF03, pour vous éviter d'avoir à gérer une BD, vous stockerez les instances de cette classe User dans une collection que vous placerez dans le contexte applicatif. La différence est que c'est maintenant Spring qui gère ce contexte et que vous n'avez donc qu'à la déclarer comme un bean Spring.

En vous inspirant de [ce tutoriel](https://www.baeldung.com/java-dao-pattern), créez un DAO qui permettra de stocker des User (attention, l'implémentation de User est un peu différente de celle du tuto). Déclarez ensuite ce DAO comme un bean Spring en utilisant la méthode de votre choix (*cf*. cours).

<!--Pour facilier la correction, il vous est demandé de respecter l'interface du DAO qui vous est donnée.-->

### Contrôleur Web MVC

Dans cette partie, vous réutiliserez votre travail sur la création et la vérification de tokens JWT du TP4 de M1IF03. En fallback, vous placerez une chaîne de caractères avec le login de l'utilisateur dans le header authentification.

Réalisez un contrôleur Spring annoté. Pour vous faciliter la tâche, les prototypes des méthodes `login()` et `authenticate()` sont donnés. &Agrave; vous de les implémenter.

Créez également une méthode `logout()` sur le même modèle.

Testez.

### REST

En vous inspirant de [ce tuto](https://spring.io/guides/gs/rest-service/), mettez en place un contrôleur REST qui permet d'accéder à un utilisateur. Ce contrôleur fera appel au bean défini à la question précédente pour accéder à la liste des utilisateurs.

Testez.

Une fois la méthode GET réalisée, améliorez-le pour qu'il permette les opérations CRUD classique sur un utilisateur :

- POST "/users" -> création
- PUT "/users/{login}" -> mise à jour du mot de passe
- DELETE "/users/{login}" -> Suppression

On ne s'occupe pas ici du contrôle des autorisations d'accès à ce contrôleur.

Testez.

### Négociation de contenus

Dans cette partie, vous allez utiliser les paramètres `consumes` et `produces` des annotations de méthodes pour mettre en oeuvre la négociation de contenus.

En rajoutant des méthodes annotées, faites en sorte que votre contrôleur accepte les contenus en JSON et URL-encoded, et puisse générer des réponses en JSON, XML ou HTML.

Indications :
- Pour produire du HTML, vous utiliserez le moteur de templating [ThymeLeaf](https://www.thymeleaf.org/), que vous configurerez à l'aide d'annotations et d'une classe de configuration (voir [ce tutoriel](https://www.baeldung.com/thymeleaf-in-spring-mvc)).
- Pour spécifier le type de contenu renvoyé par défaut (JSON), vous utiliserez un [WebMvcConfigurer](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/WebMvcConfigurer.html#configureContentNegotiation-org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer-) (voir la premère réponse à [ce post sur StackOverflow](https://stackoverflow.com/questions/55695412/how-to-set-priority-to-spring-boot-request-mapping-methods)).

Testez.

## Gestion des erreurs

Spring offre plusieurs possibilités pour la gestion des erreurs. Vous choisirez l'une de celles-ci :

- la gestion des erreurs dans les contrôleurs, comme indiqué dans [la solution 4 de ce tuto](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- un contrôleur commun dont la responsabilité sera de renvoyer les erreurs HTTP correspondant aux exceptions dans le code, comme indiqué dans [ce tuto](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)

Testez.

## Déploiement sur votre VM

Mettez en place un script `.gitlab-ci.yml` similaire à celui que vous avez fait pour M1IF03, pour déployer le fichier war de votre application sur le serveur Tomcat de votre VM. Testez.
