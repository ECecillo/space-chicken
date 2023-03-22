import axios, { AxiosRequestConfig } from 'axios';

import { rootUrl } from './env.config';

const api = axios.create({
  baseURL: rootUrl,
  headers: {
    post: 'application/json',
    Origin: 'http://localhost:8080',
  },
});

/* const token = sessionStorage.getItem('jwt');

if (token) {
  api.defaults.headers.common.Authorization = `Bearer ${token}`;
}
*/
export const apiRequest = async (config: AxiosRequestConfig) => {
  try {
    const response = await api(config);
    return response;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
