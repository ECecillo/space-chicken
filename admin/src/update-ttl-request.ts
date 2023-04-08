import { apiPath } from './constants';

export async function updateTTLRequest({ token }, ttl: string) {
  const headers = new Headers();
  headers.append('Content-Type', 'application/json');
  headers.append('Authorization', token);
  const body = {
    ttl,
  };

  const requestConfig = {
    method: 'PUT',
    headers,
    body: JSON.stringify(body),
    mode: 'cors',
  } as const;

  const response = await fetch(`${apiPath}/admin/ttl`, requestConfig);
  if (response.status !== 204)
    throw new Error(`Bad response code (${response.status}).`);

  console.log('mise à jour du ttl réussi');
  alert('Mise à jour du ttl réussi');
}
