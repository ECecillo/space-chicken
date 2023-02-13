# M1if13 2023

Ce dépôt contient les énoncés et le code de base des TPs de l'UE "Web Avancé, Web Mobile" de M1 informatique, pour le second semestre 2022-2023.

## Principe général de l'UE

Comme M1IF03, cette UE se déroule en mode projet, ce qui signifie que vous aurez à réaliser une application complète tout au long de l'UE. Cette application est un jeu qui sera réalisé sous forme d'application Web mobile, et utilisera les différentes technos enseignées dans cette UE et en M1IF03. Comme au semestre précédent, vous utiliserez une approche DevOps et aurez à la déployer sur une VM de l'infrastructure OpenStack du département.

Voici le programme* des TPs :

- **Bloc 1 : prog. serveur avancée en Java**
  - 13/02 : [framework côté serveur (Spring Boot)](tp1)
  - 22/02 : conception et spécification d'une API (annotations OpenAPI)
- **Bloc 2 : prog. serveur en JS**
  - 15/03 : stack JS (Node, Express, Webpack, EsLint)
  - 22/03 : infrastructure (déploiement sur OpenStack, requêtage en CORS)
- **Bloc 3 : prog client avancée**
  - 12/04 : framework côté client (VueJS)
  - 19/04 : state management pattern (VueX)
- **Bloc 4 : Web mobile**
  - 10/05 : utilisation des capteurs et actionneurs du client (Device APi)
  - 17/05 : Progressive Web Apps (PWA)
- **Bloc 5 : dernières optimisations et démos**
  - 07/06 : Web Assembly (WASM)
  - 14/06 : CCF + Démos

(*) Ce programme n'est pas définitif et peut encore changer.

## Architecture générale de l'application

![Architecture et technos utilisées](archi.png)

## Gestion de projet

### Binômes et VMs

Merci de renseigner le numéro de votre binôme (qui commence par un chiffre) sur Tomuss.

Nous vous affecterons ensuite un ID de binôme de la forme `grXX`, et une VM dont le nom contiendra cet ID.

### Channels Rocket chat

Plusieurs channels ont été créés ou le seront en fonction des besoins :

- [général](https://go.rocket.chat/invite?host=chat-info.univ-lyon1.fr&path=invite%2FjqdPdB)
- [TP](https://go.rocket.chat/invite?host=chat-info.univ-lyon1.fr&path=invite%2F5k69H3)
- temporaires...

### Projet forge

Créez un projet sur la forge, avec les caractéristiques habituelles (notamment le `.gitignore`). Vous travaillerez en suivant la méthode qui vous correspond le mieux, mais au final, **seules seront évaluées les fonctionnalités poussées sur la branche `master`**.

Rappels :

- Ne poussez pas les fichiers générés / téléchargés, notamment les répertoires `target` et `node_modules`
- Ne forkez pas le projet contenant les énoncés
- Initialisez le projet avec un `README.md` comportant au moins les numéros et noms des étudiants et l'ID de votre binôme, et mettez-le à jour dès que vous faites un choix de conception / déploiement qui n'est pas explicitement indiqué dans l'énoncé d'un TP

&Agrave; la fin de chaque TP, vous tagguerez le dernier commit avec le numéro du TP, en majuscules (ex : "TP4").

## Infrastructure

Comme en M1IF03, vous mettrez en place une infrastructure de production sur une VM OpenStack qui vous sera attribuée par vos enseignants. Vous déploierez plusieurs modules sur cette infrastructure :

- nginx (port 80) : serveur de fichiers statiques + reverse proxy
- Spring Boot (Tomcat sur port 8080) : gestion des utilisateurs
- Node Express (port 3000) : fonctionnalités côté serveur spécifiques au métier de l'application

**Remarques** :
- votre proxy permettra uniquement d'accéder à Express, mais pas à Tomcat. Tomcat jouera le rôle d'un serveur d'authentification centralisé (CAS) indépendant de l'application, comme celui l'UCBL.
- le certificat HTTPS qui vous sera donné est indépendant du numéro de port ; vous pourrez donc utiliser le même pour nginx et Tomcat.

### Mise en place de la VM

Vous installerez votre VM en grande partie comme cela a été fait en M1IF03 :

- Nginx (cf. [M1IF03 TP1](https://perso.liris.cnrs.fr/lionel.medini/enseignement/M1IF03/#md=TP/md/TP2)) :
  - installation du certificat HTTPS sur nginx
  - configuration en reverse proxy pour le serveur Node/Express (port 3000, URL exposée : "admin")
- Tomcat :
  - préinstallé sur la VM sur le port 8080
  - installation du certificat HTTPS : voir [cette page](tomcat-https.md)
- Express :
  - vous n'avez rien à faire que de vérifier que le serveur sera bien lancé sur le port 3000

Une fois la configuration faite, vérifiez que les 3 serveurs fonctionnent correctement. Pour nginx, vérifiez d'une part le serveur de fichiers statiques et d'autre part le reverse proxy. Pour Tomcat, vérifiez qu'il répond sur les ports 8080 et 8443.

### Intégration continue

Dès que vous aurez une VM attribuée et fonctionnelle, vous mettrez en place un runner GitLab CI qui permettra de déployer et de tester votre travail à chaque push sur la branche main.

