import { apiPath } from './constants';
import { Coordinate } from './types/resources';

export async function updateGoldingueRequest({ token }, positionGoldingue: Coordinate) {
  const headers = new Headers();
  headers.append('Content-Type', 'application/json');
  headers.append('Authorization', token);
  const body = {
    position: positionGoldingue,
  };

  const requestConfig = {
    method: 'POST',
    headers,
    body: JSON.stringify(body),
    mode: 'cors',
  } as const;

  const response = await fetch(`${apiPath}/admin/goldingue`, requestConfig);
  if (response.status !== 200)
    throw new Error(`Bad response code (${response.status}).`);

  console.log('Création goldingue réussie');
  alert('Goldingues créés avec succès');
}
