# TP 5 - Framework côté client

## Objectifs

  - Prendre en main un framework en JS : Vue.js
  - Expérimenter les APIs de stockage de données en JS

## Pointeurs

- **Framework**
  - [Vue.js](https://vuejs.org/) : le framework qui sera utilisé pour le client du jeu
  - [Vue-router](https://router.vuejs.org/) : le routeur dédié à Vue
- **APIs JS**
  - API Web Storage :
    - [Spec](https://html.spec.whatwg.org/multipage/webstorage.html#webstorage) (HTML Living Standard)
    - [Doc](https://developer.mozilla.org/fr/docs/Web/API/Web_Storage_API) (MDN)
- **Outils de développement**
  - [create-vue](https://github.com/vuejs/create-vue) : un configurateur pour Vite dédié à Vue
  - [Vite (JS)](https://vitejs.dev/) : un outil de scaffolding ("échafaudage") de projets JS, qui génère un un projet tout configuré, propose un serveur de dev, permet de builder...
  - [Vue.js devtools](https://github.com/vuejs/vue-devtools) : plugin de navigateur qui facilite le développement/debug des projets Vue<br>Attention, choisissez le plugin conçu pour la version 3 de Vue, car celui pour la version 2 ne détectera pas le framework dans votre application.

## Application

Dans ce TP, vous allez commencer à réaliser le client pour la partie publique du jeu. Cet énoncé part du principe que :
- la gestion des utilisateurs est disponible en HTTPS (si vous n'avez pas réussi à installer le certificat sur Tomcat, l'interroger à partir de l'URL `https://proxy-tps-m1if13-2019.univ-lyon1.fr/XX/`)
- l'API publique du jeu côté serveur fonctionne, et qu'elle est déployée en HTTPS via le serveur nginx sur la route `/api/game`
- l'interface "confidentielle" (d'administration) fonctionne également pour pouvoir configurer le jeu ; elle ne sera pas utilisée dans ce TP mais vous en aurez besoin pour tester.

**Remarque** : dans ce TP, vous ne travaillerez pas avec de "vraies" données. Ce TP va vous permettre de mettre en place un framework, mais la gestion des données sera mise en place au TP6 sur le _State Management Pattern_ Vue. Dans ce TP, **vous travaillerez avec des données mockées** pour ce qui est de la remontée de la position des joueurs.

## 1. Création d'une application Vue.js

### 1.1. Initialisation avec Vite

Suivez les instructions [ici](https://vuejs.org/guide/quick-start.html#creating-a-vue-application) pour créer une application, puis lancer Vite qui procèdera à l'installation et à l'exécution de votre application.

Créez un projet Vue avec les caractéristiques suivantes :

```
√ Project name: ... client
√ Add TypeScript? ... No / Yes -> à vous de voir
√ Add JSX Support? ... No
√ Add Vue Router for Single Page Application development? ... Yes
√ Add Pinia for state management? ... Yes -> sera utilisé au TP6
√ Add Vitest for Unit Testing? ... No
√ Add an End-to-End Testing Solution? » No
√ Add ESLint for code quality? ... Yes
√ Add Prettier for code formatting? ... Yes
```

Puis suivez les indications jusqu'au lancement du serveur de dev, qui doit se lancer et servir une application de démo avec Vue.

### 1.2. Première application Vue

Dans votre navigateur, ouvrez un onglet à l'URL http://localhost:5173 et observez l'application générée. Dans les Dev tools, vous devriez avoir un nouvel onglet "Vue" qui vous informe sur l'application, les composants etc.

Ouvrez un IDE dans le répertoire du projet. Commencez par regarder le contenu des fichiers de configuration du projet `package.json` et `vite.config.js`.

Depuis votre IDE, changez le contenu du composant HelloWorld. Après quelques secondes, vous devriez voir la page se modifier dans votre navigateur.

## 2. Prise en main de Vue

_Cette partie est destinée à vous aiguiller si vous n'avez jamais utilisé Vue ou un autre framework côté client. Elle vous permet de commencer à mettre en place votre application avec des instructions et des pointeurs. Si vous êtes à l'aise avec Vue ou souhaitez le découvrir par vous-même, vous pouvez passer cette partie._

Comme vous l'avez vu dans le projet généré, une application Vue est une arborescence de composants, dont la racine est celui lancé dans le fichier `main.js`. Chaque composant Vue contient une partie de votre application, avec un template, un comportement (script) et un style.

### 2.1. Premières modifications de composants

Dans un premier temps, vous modifierez le composant principal (`App.vue`) pour qu'il affiche le formulaire de login si l'utilisateur n'est pas connecté, ou le reste de l'application sinon. L'idée générale est de créer une variable booléenne et de modifier template pour que le rendu s'adapte aux changements d'états de cette variable. Le problème que vous allez rencontrer est dû au fait que le rendu est calculé au chargement du composant...

Dans un premier temps :

- Dans `<script>`, créez une variable booléenne nommée `logged` qui indiquera si l'utilisateur est logué. Initialisez-la à false.
- Dans `<template>`, inspirez-vous de la section [Conditional rendering](https://vuejs.org/guide/essentials/conditional.html) de la doc pour afficher un texte simple si la variable vaut `false` et le contenu initial sinon.

Vérifiez le fonctionnement en modifiant la valeur initiale et sauvegardant le fichier. Le serveur de dev va recharger l'application et le template réagira donc en conséquence.

- Ajoutez le code suivant au template (dans le header, avant les traitements conditionnels) :<br>
`<button @click="logged = !logged">Toggle login</button>`
- Vérifiez que quand vous cliquez sur le bouton, la valeur de la variable est modifiée : dans Vue DevTools, cliquez sur le composant App pour visualiser toutes les variables et données le concernant.

Mais cela, ne doit pas modifier l'aspect de votre composant dynamiquement. Pour que ce soit le cas, il faut rendre le template "réactif" aux changements d'états de `logged`. Plusieurs solutions (voir [doc de l'API de vue](https://vuejs.org/api/)) :

- Faire renvoyer la variable par une fonction de gestion des états : `data`, `props`, `computed`... (**Options API**)
- Rendre la variable mutable avec [`ref()`](https://vuejs.org/api/reactivity-core.html#ref) (**Composition API**)

**Remarque :** pour l'API Options, les fonctions doivent se trouver dans un élément `<script>` et non ~~`<script setup>`~~. Voir la différence [ici](https://vuejs.org/api/sfc-script-setup.html#script-setup) et la coexistence entre les 2 [là](https://vuejs.org/api/sfc-script-setup.html#usage-alongside-normal-script). Notez qu'il faut _impérativement_ que `<script>` précède `<script setup>`.

### 2.2. Modification de l'arborescence des composants

Pour vous faire gagner du temps, vous trouverez dans le dossier [tp4](.) :
- la base d'un composant permettant de vous loguer (script à compléter) : `Login.vue`
- un composant qui permet d'affichier la carte avec Leaflet : `MyMap.vue`. Pour qu'il puisse afficher la carte, le package NPM leaflet doit avoir été préalablement installé : `npm install --save leaflet`

Travail à réaliser :
- Déplacer et dupliquer le composant `HelloWorld` dans chaque bloc (logué ou non logué) en lui passant dans la propriété `msg` une string correspondant à la situation
- Afficher le composant de login dans le cas où `logged` vaut `false`.
- Compléter le processus de login avec Spring et passer `logged` à `true` si un token est reçu.

### 2.3. Propriétés

- Modifiez le composant `<Login>` pour qu'il inclue une propriété `message` qui sera affichée pour indiquer s'il y a eu une erreur de login lors d'une tentative précédente.
- Dans le composant de niveau précédent (`<App>`), créez une variable contenant un message et passez cette variable en tant que propriété à `<Login>`.
- Faites en sorte que `<Login>` affiche ce message.
- Faites en sorte que le clic sur le bouton du formulaire dans `<Login>` déclenche une modification de la valeur de la variable dans `<App>`.

**Aide :** comme indiqué [ici](https://vuejs.org/guide/components/props.html#one-way-data-flow), la transmission des propriétés ne se fait que de manière descendante, d'un composant à ses fils. Pour "remonter" une information, il faut utiliser un pattern observer qui [permet aux composants de s'envoyer des événements](https://vuejs.org/guide/components/events.html). Cela permet au callback qui s'abonne à un événement se trouve dans un autre composant que celui qui l'a envoyé.

- Dans le callback, envoyez la requête d'authentification en suivant l'exemple [ici](https://play.vuejs.org/#eNptUk2PmzAQ/SuvXAA1sdVrmt0qqnroqa3UIxcLhuCGjKk/wkYR/70OBJLuroRkPDPvzbznuSS7rhOnQMkm2brS6s4/F0wvnbEeFdUqtB6XgoFKeZXl0z9gyQfL8w34G8h5bXiDNF3NQcWuJxtDv25Zh+CCatszSsNeaYZakDgqexD4vM7TCT9cj2Ek65Uvm83cTUr0DTGdyN7RZaN4T24F32iHOnA5hnvdtrCBJ+RcnTH180wrmLaaL4s+QNd4LBOaK3r5UWfplzTHM9afHmoxdhV78rtRcpbPmVHEf1qO5BtTuUWNcmcu8QC9046kk4l4Qvq70XzQvBdC3CyKJfb8OEa01fn4OC7Wq15pj5qidVnaeN+5jZRncmxE72upOp0uY77ulU3gSCT+uOhXnt9yiy6U1zdBRtYa+9aK+9TfrgUf8NWEtgKbK6mKQN8Qdj+/C6T4iJHkXcsKjt9WLpsZL56OXas8xRuw7cYD2LlDXKYoT7K5b+OU22rugsdpfTQVtU9FMueLBHKikRNPpLtcbnuLYZjCW7m0TIZ/92UFiQ==)
- Supprimez le bouton "toggle" mis en place plus haut et remplacez-le par un bouton "logout" qui ne sera placé que dans la partie poru laquelle `logged` vaut `true`.

### 2.4. Routeur

Lorsque vous avez "scaffoldé" votre projet, vous avez demandé à `create-vue` de mettre en place le routeur de Vue. Le projet généré l'utilise pour faire de votre application une Single-Page Application, à l'aide des composants `RouterLink` et `RouterView`.

**Remarque** : le routeur de Vue permet de faire du routage côté client, même si les paths commencent par un slash (les URLs sont en http://serveur/route).

Modifiez le menu et la configuration du routeur pour ajouter une route contenant le composant MyMap.

**Aide** : contrairement à l'exemple simple qui vous a été présenté en M1IF03, le routeur de Vue ne se contente pas de changer les propriétés CSS des éléments qui ne sont pas dans la route courante pour ne pas qu'ils soient visibles. Il les supprime du DOM et les met en les "cache". En conséquence, certaines parties de vos composants ne seront pas accessibles quand les composants sont "arrêtés" et les hooks liés à leur cycle de vie sont rappelés quand vous revenez sur la route à laquelle ils appartiennent. De la même façon, un composant n'a pas été initialisé tant que sa route n'a pas été activée. Cela pose plusieurs problèmes :

  - Leaflet n'est pas prévu à l'origine pour être affiché dans un composant Vue, et __a fortiori__ pas dans une SPA avec un routeur qui "éteint" les composants quand ils ne sont pas affichés. Il faudra prévoir de créer puis de replacer la map aux bonnes coordonnées et au bon niveau de zoom chaque fois que la route change et qu'elle doit être réaffichée.
  - Si certains éléments comme les rectangles sont automatiquement réaffichés avec la map (vous pouvez donc les ajouter avec `addTo()` et ne plus vous en préoccuper), les markers ne fonctionnent pas de la même façon (non documenté). Pour afficher des markers "routing-proof", vous devrez donc :
    - mémoriser (localement ou dans le store) les données de chacun des markers
  - recréer ces markers et les ajouter à la map à chaque nouvel affichage (hook : `bounded`) du composant
  - supprimer ces markers et les retirer de la map à chaque changement de route et arrêt (hook : `beforeDestroy`) du composant
  - Apparemment, les bindings des événements dans Vue ne sont pas gérés de la même façon en fonction de leur target :
    - les bindings aux événements globaux (Event bus) sont conservés entre les créations du composant, même si ce composant est démarré et arrêté par le routeur. Donc si vous faites un binding dans un hook (`created` ou `mounted`), vous déclencherez autant de fois le callback que vous aurez redémarré le composant. Solution : mettre dans le module un booléen qui vérifie que les bindings n'ont été faits qu'une fois.
  - les bindings des événements locaux au composant sont perdus quand on change de route. Il faut donc bien les définir dans le hook `mounted`.

## 3. Fonctionnalités à réaliser

_Voici une liste (non exhaustive) des fonctionnalités à réaliser :_

- Permettez à l'utilisateur de se loguer, et utilisez l'API Web storage pour mémoriser le token renvoyé par le serveur Spring
- Modifiez l'une des routes existantes pour permettre à l'utilisateur de modifier son profil (mot de passe, URL de l'image), et envoyer les données au serveur
- Créez un objet qui mocke les positions de tous les objets géolocalisés (joueurs et plants de goldingue)
- Sur la carte, faites apparaître :
  - la ZRR
  - les joueurs avec leurs images et un marker différent pour les poules et pour les cow-boys
  - les plants de goldingue avec un marqueur indiquant leurs TTL ou une image spécifique s'ils ont été ramassés, non ramassés mais TTL expiré, ou transformés en nids
- Créez un objet qui mocke la position du joueur local et mettez à jour l'affichage du joueur courant séparément de celle des autres joueurs
- Faites comme si les coordonnées du joueur local étaient variables, et renvoyez-les au serveur Express toutes les 5 secondes
- Lorsque le TTL d'un plant est expiré ou lorsque le joueur a rejoint un plant (à moins de 2m), faites en sorte que l'affichage affiche un nouveau composant textuel par-dessus la carte, avec les informations correspondantes.

**Aide** :
- Vous pouvez réaliser ces fonctionnalités dans les scripts des composants (méthodes ou hooks), ou bien les placer dans des fichiers JS à part si elles doivent être partagés par plusieurs composants.
- Pour pouvoir inclure des composants à un niveau quelconque de l'arborescence DOM, il faut les déclarer explicitement dans l'application Vue, à l'aide de la propriété `components` : https://stackoverflow.com/questions/39382032/vue-js-unknown-custom-element
- Quand vous rajoutez dynamiquement des éléments HTML à des composants dans des fichiers .vue, ils ne peuvent pas utiliser les styles définis pour votre composant, car ces styles sont "scoped" -> il faut rajouter l'attribut `data` du composant avec `vm.$options._scopeId` (voir [ici](https://github.com/vuejs/vue-loader/issues/559)).
