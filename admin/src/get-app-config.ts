import { apiPath } from './constants';
import { AppConfigType } from './types/resources';

export async function getAppConfig({
  token,
}: {
  token: string;
}): Promise<AppConfigType> {
  const headers = new Headers();

  headers.append('Content-Type', 'application/json');
  headers.append('Authorization', token);

  const requestConfig = {
    method: 'GET',
    headers,
    mode: 'cors',
  } as const;

  const response = await fetch(`${apiPath}/admin/`, requestConfig);
  if (response.status !== 200)
    throw new Error(`Bad response code (${response.status}).`);

  const data: AppConfigType = await response.json();
  return data;
}
