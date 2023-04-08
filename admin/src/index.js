require('purecss');
require('./css/style.css');

async function authenticateAdmin() {
  const headers = new Headers();
  headers.append("Content-Type", "application/json");
  const body = {
    login: "admin",
    password:  document.getElementById("pass").value
  };

  const requestConfig = {
    method: "POST",
    headers,
    body: JSON.stringify(body),
    mode: "cors",// pour le cas où vous utilisez un serveur différent pour l'API et le client.
  };
  try {
    const response = await fetch("http://localhost:8080/login", requestConfig)
    if (response.status === 204 && response.headers.get("Authorization")) {
      console.log("Connexion réussie", "alert-success");
      console.log(`In login: Authorization =  ${response.headers.get("Authorization")}`);
      localStorage.setItem("token", response.headers.get("Authorization")); // Stockage local du token
      return true;
    }
    console.log("Connexion refusée ou impossible", "alert-danger");
    throw new Error(`Bad response code (${response.status}).`);
  } catch (error) {
    console.error(`In login: ${error}`);
  }
  return false
}

window.login = (event) => {
  event.preventDefault(); // empêche la soumission normale du formulaire

  // récupère le mot de passe entré par l'utilisateur
  const password = document.getElementById('pass').value;

  // vérifie les informations d'identification en utilisant la fonction "authenticateAdmin()"
  authenticateAdmin(password)
    .then((result) => {
      if (result) {
        // redirection de la page vers "admin.html" si l'authentification est réussie
        window.location.href = 'admin.html';
      } else {
        alert('Mauvais mot de passe!');
      }
    })
    .catch((error) => {
      console.error(error);
      alert('Erreur lors de l\'authentification!');
    });
}