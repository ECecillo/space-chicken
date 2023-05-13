import { defineStore } from 'pinia';

import { fetchWrapper } from '../utils/fetch-wrapper';
import { authentificationURL, resourceURL } from '../api/server/env';
import { useAuthStore } from './authentification';

// Store for current player.
export const useUserStore = defineStore({
  id: 'user',
  state: () => ({
    user: {
      name: null,
      image: null,
      position: null,
      role: null,
      ttl: null,
      score: 0,
    },
    isLoading: false,
    isError: false,
  }),
  actions: {
    async loadUser() {
      const url = `${resourceURL}/api`;
      const body = null;

      try {
        this.isLoading = true;
        const resourcesResponse = await fetchWrapper.get(`${url}/resources`, body);
        const currentUserLogin = localStorage.getItem('login').toString().slice(1, -1);

        const currentUser = JSON.parse(JSON.stringify(resourcesResponse)).filter((resource) => {
            return resource.id === currentUserLogin;
          }
        );
        const userResponse = await fetchWrapper.get(`${authentificationURL}/users/${currentUserLogin}`, body);

        this.user.name = userResponse.login;
        this.user.image = userResponse.image;
        this.user.position = currentUser[0].position;
        this.user.role = currentUser[0].role;
        this.user.ttl = currentUser[0].ttl;
        this.user.score = currentUser[0].nests + currentUser[0].nuggets;
      } catch (error) {
        this.isError = true;
        throw error;
        if(error.status === 401){
          useAuthStore().logout();
        }
        throw error;
      } finally {
        this.isLoading = false;
        this.isError = false;
      }
    },
    async updateUserProfile(password, image) {
      const urlSpring = `${authentificationURL}/users/infos/` + this.user.name;
      await fetchWrapper.put(urlSpring, { password, image });
    },
    async updateUserPosition(position) {
      try {
        const url = `${resourceURL}/api`;
        const body = { position : position};
        await fetchWrapper.put(`${url}/resources/${this.user.name}/position`, body);
      } catch (error) {
        throw error;
      } finally {
        this.user.position = position;
      }
    },
    async refreshUser() {
      const url = `${resourceURL}/api`;
      const body = null;

      try {
        const resourcesResponse = await fetchWrapper.get(`${url}/resources`, body);
        const currentUserLogin = localStorage.getItem('login').toString().slice(1, -1);

        const currentUser = JSON.parse(JSON.stringify(resourcesResponse)).filter((resource) => {
            return resource.id === currentUserLogin;
          }
        );
        const userResponse = await fetchWrapper.get(`${authentificationURL}/users/${currentUserLogin}`, body);

        this.user.name = userResponse.login;
        this.user.image = userResponse.image;
        this.user.position = currentUser[0].position;
        this.user.role = currentUser[0].role;
        this.user.ttl = currentUser[0].ttl;
        this.user.score = currentUser[0].nests + currentUser[0].nuggets;
      } catch (error) {
        console.error(error);
        if(error.status === 401){
          useAuthStore().logout();
        }
      }
    }
  }
});
