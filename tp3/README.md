# TP 3 - JavaScript côté serveur

## Objectifs

Comprendre le rôle et utiliser quelques outils de gestion de projet pour mettre en place un serveur en JS

  - NPM : gestion de dépendances côté serveur
  - Node : exécution côté serveur
  - Express : serveur Web

## Pointeurs

Documentation et tutos :

  - NPM :
    - [Documentation](https://docs.npmjs.com/)
    - [CLI](https://docs.npmjs.com/cli/v7/commands)
  - Node :
    - [Guides et tutos](https://nodejs.org/en/docs/guides/)
    - [Documentation de l'API](https://nodejs.org/docs/latest-v18.x/api/) (V. 18 LTS)
  - [Express](https://expressjs.com/) :
    - [lancer un serveur](https://expressjs.com/en/starter/hello-world.html)
	  - [servir des fichiers statiques](https://expressjs.com/en/starter/static-files.html)
	  - [Routage (répondre à des URLs spécifiques)](https://expressjs.com/en/guide/routing.html)
	  - [Traiter des données de formulaire](https://www.npmjs.com/package/body-parser)
  - ESLint :
    - [installation](https://eslint.org/docs/user-guide/getting-started)
    - [configuration](https://eslint.org/docs/user-guide/getting-started#configuration)
    - [règles](https://eslint.org/docs/rules/)
 
## Description de l'application

Dans ce TP et les suivants, vous allez travailler sur une application que vous transformerez progressivement en un jeu Web mobile, multi-joueurs, où les utilisateurs seront géolocalisés et utiliseront une visualisation cartographique. Vous trouverez à la racine de ce projet le [pitch de ce jeu](../pitch.md).

Ce TP sera consacré à la mise en place de l'API côté serveur du jeu. &Agrave; la fin de ce TP :
- les joueurs devront pouvoir :
  - accéder aux informations sur les ressources exposées par le serveur :
    - coordonnées de la ZRR
    - coordonnées et TTL des plants de goldingue, et coordonnées des nids
    - positions courantes de tous les joueurs
  - remonter au serveur les informations les concernant :
    - indiquer leur position
    - déclarer qu'ils ont soit attrapé une pépite d'or (pour un cow-boy), soit bâti un nid autour d'un plant de goldingue (pour une poule)
- un administrateur pourra en plus :
  - spécifier les limites de la ZRR
  - définir le TTL initial
  - déclencher l'apparition d'un plant de goldingue
  - accéder aux informations remontées par les joueurs

Une fois la partie lancée, c'est la responsabilité du serveur de calculer le TTL des plants en les remettant à jour à chaque requête d'accès à ces ressources. Il en informe le client dans les réponses aux requêtes asynchrones.

Lorsqu'un joueur déclare avoir soit attrapé une pépite, soit bâti un nid autour d'un plant, c'est la responsabilité du serveur de vérifier que ses coordonnées sont suffisamment proches (moins de 5m) de celles de la ressource concernée avant de valider la requête.

Vous pouvez réaliser la partie sur la ZRR en dernier. Elle fait partie du barème (donc vous n'aurez pas 20 si vous ne la faites pas), mais le projet fonctionnera même si vous ne mettez pas la ZRR en place.

## Travail à réaliser

### 1. Mise en place du projet

#### 1.0. Fichiers à ignorer /!\ important

&Agrave; l'aide de [ce template](https://github.com/github/gitignore/blob/master/Node.gitignore), complétez le fichier `.gitignore` de votre dépôt avec les noms de fichiers et répertoires à ignorer pour votre projet (_a minima_ le répertoire `node_modules` et les fichiers de votre IDE).

**Avant de pusher sur la forge, vérifiez bien que le répertoire node_module n'est pas inclus dans votre `git status`.**

&Agrave; partir de ce TP, il est conseillé de travailler dans une branche bien identifiée (par exemple, "TP3"). Cela vous permettra de mettre en place des scripts de CI spécifiques et de gagner du temps au déploiement de votre application sur votre VM.

#### 1.1. Initialisation d'un projet NPM

- Créez un dossier `api` à la racine de votre dépôt de code.
- Dans ce dossier, créez un projet NPM à l'aide de [ce tuto](https://docs.npmjs.com/creating-a-package-json-file#running-a-cli-questionnaire) (inutile de "customizer" le questionniare)<br>
  Attention : utilisez la commande `npm init` sans l'option `--yes` pour pouvoir entrer autre chose que les valeurs par défaut.

Cela vous produit un fichier nommé `package.json`, sur lequel vous trouverez plus de documentation [ici](https://docs.npmjs.com/cli/v8/configuring-npm/package-json).

Comme indiqué en cours, il existe plusieurs systèmes de gestion de dépendances. Vous allez spécifier que vous choisissez d'utiliser les modules ES6. Pour cela, éditez le fichier `package.json` et rajoutez-y une propriété `"type": "module"` (n'oubliez pas la virgule à la ligne précédente).

#### 1.2. Ajout des dépendances

Avec NPM, il existe plusieurs sortes de dépendances, et notamment les dépendances "normales", qui seront téléchargées par les utilisateurs de votre projet, et les "devDependencies", qui ne sont nécessaires que pour builder, tester, etc.

Votre API nécessitera un serveur [Express](https://expressjs.com/) pour fonctionner, mais vous utiliserez aussi [ESLint](https://eslint.org/) pour valider syntaxiquement votre code (comme CheckStyle en Java).

NPM s'appuie sur un système de gestion de versions "sémantique". Voir [ici](https://github.com/npm/node-semver) si vous voulez en savoir plus sur son fonctionnement. Si vous ne spécifiez pas de version particulière, NPM installera la dernière version disponible dans le repository.


- Installez Express en dépendance simple : `npm install express`
- Installez ESLint en dépendance de dev (`npm init --save-dev @eslint/config`), et configurez-le avec les options par défaut, sauf :
  - `Which framework does your project use?` -> `None of these`
  - `Where does your code run?` -> `Node`
  - `What format do you want your config file to be in?` -> `JSON`


Dans le fichier `.eslintrc.json` généré, rajoutez _a minima_ une [règle](https://eslint.org/docs/rules/) pour spécifier que l'indentation doit être réalisée avec des tabulations. 

L'ensemble des dépendances est installé dans le dossier `node_modules`. Vous pouvez ouvrir ce dossier pour voir ce qu'il contient... et comprendre pourquoi il ne faut pas le pousser sur la forge ;-).

### 2. Partie serveur

Vous allez utiliser [Express](https://expressjs.com/) pour le back-office de votre application.

- Utilisez les tutos sur Node / Express pour démarrer un serveur Web (en mode Hello World).
- Testez
- Ajoutez au fichier `package.json` un script nommé `start` permettant de démarrer le serveur en tapant simplement `npm start` (voir [cette page](https://docs.npmjs.com/cli/v8/using-npm/scripts))
- Testez à nouveau avec ce script
- Ajoutez un script `prestart` qui validera le code avec ESLint. Ce script sera automatiquement exécuté avant `start` (cf. [Life Cycle Operation Order](https://docs.npmjs.com/cli/v8/using-npm/scripts#life-cycle-operation-order) de NPM).
- Testez à nouveau `npm start`. Dites à ESLint de se débrouiller pour corriger l'indentation automatiquement. Il vous reste une erreur à corriger vous-même : passer du système de gestion de dépendances CommonJS à celui des modules ES6...
- Corrigez et retestez.

#### 2.1. Serveur de fichiers statiques

- Faites en sorte que votre serveur Express réponde aux requêtes sur `/static` en servant directement les fichiers situés dans le répertoire `public`.
- Créez une page d'accueil HTML basique, et placez-la dans le répertoire `public` pour tester que vous pouvez la requêter avec votre serveur Express.
- Redirigez la requête à la racine du serveur vers cette page d'accueil
- Ajoutez un [middleware de gestion des erreurs 404](http://expressjs.com/en/starter/faq.html#how-do-i-handle-404-responses)
- Testez

#### 2.2. Contenus dynamiques

Dans cette partie, vous mettrez en place l'API côté serveur qui va gérer les ressources géolocalisées : les utilisateurs et autres éléments placés par le serveur.

Voici les différents types de ressources gérées par le serveur (fournis à titre indicatif) :

```
ressource géolocalisée
|__id
|__position
|__role
   |__cow-boy
   |  |__nombre de pépites
   |__poule
   |  |__nombre de nids
   |__goldingue
   |  |__TTL
   |__nid
ZRR
|__limite-NO
|__limite-NE
|__limite-SE
|__limite-SO
```

Vous allez mettre en place 2 parties de votre application capables de [servir des contenus dynamiques](http://expressjs.com/en/starter/basic-routing.html), une pour gérer le fonctionnement du jeu, et une autre pour l'interface d'administration.

Pour chacune de ces 2 parties de l'application, vous créerez un middleware spécifique, à l'aide de la classe [`express.Router`](http://expressjs.com/en/guide/routing.html#express-router), et l'associerez à une route spécifique (voir partie Déploiement). Vous placerez le code de ces routes dans un sous-répertoire `routes`.

##### 2.2.1. Gestion du fonctionnement du jeu

4 requêtes sont à implémenter dans le premier routeur :

- Mise à jour de la position du joueur : le format utilisé pour les positions est un tableau de deux décimaux tel que défini par l'API de cartographie qui sera utilisée par les clients. Exemple : `[45.781987907026334, 4.865596890449525]`.
- Récupération de la liste complète des ressources géolocalisées à afficher : renvoie le tableau des ressources pour lesquelles le serveur dispose d'informations de géolocalisation, avec les attributs de chaque ressource. Cela comprend les joueurs qui ont indiqué leurs positions (et pas les autres, ni l'administrateur), les plants de goldingue dont le TTL est >0, et les nids. Toutes les ressources ont un ID (login pour un user, string pour les autres), une position, plus d'autres attributs, dépendants du "rôle" (pour ne pas dire "type") de cette ressource.
- Action sur un plant de goldingue : récupération de la pépite ou construction d'un nid. Ne pourra être validée que si l'utilisateur est suffisamment proche (moins de 5m) du plant, si son TTL est >0, et si l'opération correspond au rôle de l'utilisateur.
- Récupération des limites de la ZRR : si elle a été définie par l'administrateur (voir plus bas)

L'API du serveur est donnée dans le fichier [`express-api.yaml`](./express-api.yaml). Vous pouvez vous aider de la génération de serveurs pour générer le squelette de votre code.

&Agrave; la réception de chaque requête, n'oubliez pas de valider l'identité de l'utilisateur avec le serveur Spring :

- pour le requêtage du serveur Spring, vous pouvez utiliser [Axios](https://www.npmjs.com/package/axios)
- pour éviter de vous e...er avec la configuration d'Axios lorsque vous requêtez le serveur Spring (dont le certificat est signé par une autorité qu'il ne connaît pas), vous pouvez le requêter sur le port 8080.

##### 2.2.2. Interface d'administration

3 requêtes sont à implémenter dans le second routeur :

- fixer le périmètre de jeu (ZRR), par exemple, en spécifiant 2 points, et en choisissant un rectangle dont ces points sont les coins
- préciser le TTL initial (valeur par défaut : 1 minute)
- déclencher l'apparition d'un plan de goldingue

Remarque : l'utilisateur administrateur devra de toutes façons être authentifié pour pouvoir visualiser la position des ressources _via_ les requêtes GET de la partie précédente (il en aura besoin au TP4). Il donc est conseillé de valider également son authentification et de limiter l'accès des 3 requêtes ci-dessus à l'administrateur. 

**Indication** : plus de détails sur l'implémentation du client de cette interface seront donnés au [TP4](../tp4/README.md)

### 3. Déploiement

- Configurez votre application pour qu'Express réponde sur le port 3376 (port ouvert sur le firewall de l'OpenStack).
- Configurez votre applicataion pour qu'Express réponde aux routes ci-dessous :
  - `/api` pour la partie publique
  - `/admin` pour la partie confidentielle
  - `/static` pour les contenus statiques (pour faciliter les tests)

Comme au premier semestre (et si ça n'a pas été fait au TP précédent) :

- Configurez votre serveur nginx en HTTPS ; vous trouverez les certificats dans le sous-répertoire `Certificats` de la [page d'accueil de l'UE](https://perso.liris.cnrs.fr/lionel.medini/enseignement/M1IF13/).
- Configurez votre serveur nginx en reverse proxy ; déployez la partie serveur de votre projet sur une route `/game` de votre proxy nginx.

**Aide** : pour faciliter l'installation d'Express en tant que service sur votre VM, vous pouvez utiliser le package [PM2](https://www.npmjs.com/package/pm2).

### 4. CI/CD

Créez un script de CI spécifique à la branche sur laquelle vous travaillez et qui recopie vos fichiers dans un répertoire donné, puis exécuter les commandes NPM pour télécharger les dépendances et (re)lancer l'application.
