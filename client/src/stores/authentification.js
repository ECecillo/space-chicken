import { defineStore } from 'pinia';

import { fetchWrapper } from '../utils/fetch-wrapper';
import { router } from '../router/router';
import { authentificationURL } from '../api/server/env';

export const useAuthStore = defineStore({
  id: 'auth',
  state: () => ({
    // check user in local storage for authentication and stay logged in if token is valid
    user: {
      login : localStorage.getItem('login'),
      token: localStorage.getItem('token'),
  }}),
  actions: {
    async login(login, password) {
      const url = `${authentificationURL}/login`;
      const body = null;
      const userAuthentificationResponse = await fetchWrapper.post(url, { login, password });
      const userResponse = await fetchWrapper.get(`${authentificationURL}/users/${login}`, body);

      // update pinia state (like a useContext hook)
      this.user = {
        login: userResponse.login,
        token: userAuthentificationResponse.headers.get('Authorization'),
      };

      // store user details and jwt in local storage to keep user logged in between page refreshes
      localStorage.setItem('login', login);
      localStorage.setItem('token', userAuthentificationResponse.headers.get('Authorization'));

      // redirect to previous url or default to home page
      await router.push('/');
    },
    logout() {
      this.user = null;
      localStorage.removeItem('login');
      localStorage.removeItem('token');
    }
  }
});
