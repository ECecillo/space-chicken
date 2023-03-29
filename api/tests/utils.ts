import { AxiosRequestConfig } from 'axios';

import { apiRequest } from '../src/config/axios.config';
import { extractBearerToken } from '../src/utils/extract-bearer-token';

export const signInUser = async (loginCredentials: {
  login: string;
  password: string;
}) => {
  const config: Partial<AxiosRequestConfig> = {
    method: 'POST',
    url: '/login',
    params: {
      Origin: 'http://localhost:8080',
    },
    data: {
      ...loginCredentials,
    },
  };
  try {
    const result = await apiRequest(config);
    const jwtToken = extractBearerToken(result.headers.authorization);
    if (!jwtToken) throw new Error('No token provided by the server.');
    return jwtToken;
  } catch (error) {
    // Specific error handling if axios couldn't reach the server.
    if (error.message === 'connect ECONNREFUSED 127.0.0.1:8080')
      throw new Error('Spring Server not Reachable verify if running.');
    else if (error.response.status === 404)
      throw new Error('User not found, please check your credentials');
    else throw new Error('Unexpected error');
  }
};
