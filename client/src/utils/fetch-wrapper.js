import { useAuthStore } from '../stores/authentification';
import { resourceURL, authentificationURL } from '../api/server/env'

export const fetchWrapper = {
  get: request('GET'),
  post: request('POST'),
  put: request('PUT'),
  delete: request('DELETE')
};

function request(method) {
  return (url, body) => {
    const requestOptions = {
      method,
      headers: authHeader(url)
    };
    if (body) {
      requestOptions.headers['Content-Type'] = 'application/json';
      requestOptions.body = JSON.stringify(body);
    }
    return fetch(url, requestOptions).then(handleResponse);
  }
}

function authHeader(url) {
  const { user } = useAuthStore();
  const isLoggedIn = !!user?.token;
  const isApiUrl = url.startsWith(authentificationURL) || url.startsWith(resourceURL);
  if (isLoggedIn && isApiUrl) {
    return { Authorization: `${user.token}` };
  } else {
    return {};
  }
}

function handleResponse(response) {
  if (response.status === 204) {
    return response;
  }
  if (response.status === 200) {
    return response.json();
  }
  if (response.status === 401) {
    // auto logout if 401 (unAuthorized) response returned from api
    useAuthStore().logout();
  }
  throw new Error('Request failed')
}
