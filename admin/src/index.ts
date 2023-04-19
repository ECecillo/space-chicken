import { authenticateAdmin } from './authenticate';
import { getHTMLInputElementValue } from './utils/get-html-element-value';

require('purecss');
require('./css/style.css');

global.login = (event: Event) => {
  event.preventDefault(); // empêche la soumission normale du formulaire

  // récupère le mot de passe entré par l'utilisateur
  const password = getHTMLInputElementValue('passwordInput');
  // const password = (document.getElementById('passwordInput') as HTMLInputElement)
  //   .value;

  // vérifie les informations d'identification en utilisant la fonction "authenticateAdmin()"
  authenticateAdmin(password)
    .then(() => {
      // redirection de la page vers "admin.html" si l'authentification est réussie
      window.location.href = 'admin.html';
    })
    .catch((error) => {
      console.error(error);
      alert("Erreur lors de l'authentification!");
    });
};
