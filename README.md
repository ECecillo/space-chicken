# M1if13 2023

## TP1 & TP2 : framework côté serveur (Spring Boot)

 - [lien fichier yaml sur le dépôt.](https://forge.univ-lyon1.fr/p1805901/space-chicken/-/blob/main/users-api.yaml)
 - [Swagger généré par Spring et déployé sur la VM.](https://192.168.75.14:8443/users/swagger-ui/index.html#/)

## TP3 : Node et Express

## TP4 : Nginx
- url de l'application (OLD): https://192.168.75.14/secret/
- url de l'application en ligne: https://space-chicken.games/secret

## TP6-7 : VueJs
- url de l'application (OLD): https://192.168.75.14/
- url de l'application en ligne: https://space-chicken.games

## TP8 : PWA
La PWA est installable sur la version en ligne https://space-chicken.games, sur navigateur ou mobile.

# Partie TEST ET DEV

1. Cloner le dépôt

### Partie Spring : 
> cd users

> mvn spring-boot:run

### Partie VueJs (client) :
> cd client

> npm install

> npm run build

> npm run dev

### Partie NodeJs (API) :
> cd api

> npm install

> npm run dev

### Partie Admin : 
> cd admin

> npm install

> npm run build

> npm run serve


Les .env ont été ajoutés pour faciliter le lancement des applications en mode développement (pour l'évaluation).
Ce n'est pas une bonne pratique, normalement ces valeurs doivent être fournies dans un vault.
