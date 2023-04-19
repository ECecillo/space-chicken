import { apiPath } from './constants';
import { AppResourcesType } from './types/resources';

export async function getAppResources({
  token,
}: {
  token: string;
}): Promise<AppResourcesType> {
  const headers = new Headers();

  headers.append('Content-Type', 'application/json');
  headers.append('Authorization', token);

  const requestConfig = {
    method: 'GET',
    headers,
    mode: 'cors',
  } as const;

  const response = await fetch(`${apiPath}/api/resources/`, requestConfig);
  if (response.status === 401) {
    alert('vous allez être déconnecté');
    window.location.href = 'index.html'; // rediriger vers la page index
  }
  if (response.status !== 200)
    throw new Error(`Bad response code (${response.status}).`);

  const data: AppResourcesType = await response.json();
  return data;
}
