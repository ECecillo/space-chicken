import { authPath } from './constants';

export async function authenticateAdmin(password: string) {
  const headers = new Headers();
  headers.append('Content-Type', 'application/json');

  const body = {
    login: 'admin',
    password,
  };

  const requestConfig = {
    method: 'POST',
    headers,
    body: JSON.stringify(body),
    mode: 'cors', // pour le cas où vous utilisez un serveur différent pour l'API et le client.
  } as const;

  try {
    const response = await fetch(`${authPath}/login`, requestConfig);
    if (response.status !== 204)
      throw new Error(
        `Connexion refusée ou impossible : Bad response code (${response.status}).`,
      );
    if (!response.headers.get('Authorization'))
      throw new Error('Connexion refusée ou impossible : Pas de token.');

    console.log('Connexion réussie', 'alert-success');
    localStorage.setItem('token', response.headers.get('Authorization')); // Stockage local du token
  } catch (error) {
    console.error(`In login: ${error}`);
    throw error;
  }
}
