import { apiPath } from './constants';
import { newCoordinates } from './types/resources';

export async function updateZRRRequest(
  { token }: { token: string },
  body: { newPosition: newCoordinates },
) {
  const headers = new Headers();
  headers.append('Content-Type', 'application/json');
  headers.append('Authorization', token);

  const requestConfig = {
    method: 'PUT',
    headers,
    body: JSON.stringify(body),
    mode: 'cors',
  } as const;

  const response = await fetch(`${apiPath}/admin/zrr`, requestConfig);
  if (response.status !== 200)
    throw new Error(`Bad response code (${response.status}).`);

  alert(`Mise à jour de la zrr réussi `);
}
