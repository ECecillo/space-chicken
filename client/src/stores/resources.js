import { defineStore } from 'pinia';

import { fetchWrapper } from '../utils/fetch-wrapper';
import { authentificationURL, resourceURL } from '../api/server/env';
import goldingue from '@assets/icons/goldingue.png';
import nest from '@assets/icons/nest.png';
import nuggets from '@assets/icons/nuggets.png';
import { useAuthStore } from './authentification';
import { useUserStore } from './user';
import GeoUtils from '../utils/geoUtils';

// Store for all resources and players of the current game.
export const useResourcesStore = defineStore({
  id: 'resources',
  state: () => ({
    resources : [],
    zrr: [],
    isLoading: true,
    isError: false,
  }),
  actions: {
    async loadResources() {
      const url = `${resourceURL}/api`;
      const urlImage = `${authentificationURL}/users`;
      const body = null;

      try {
        const resourcesResponse = await fetchWrapper.get(`${url}/resources`, body);
        const zrrResponse = await fetchWrapper.get(`${url}/resources/zrr`, body);
        this.resources = resourcesResponse.filter((resource) => {
          if(resource.role === 'goldingue' && resource.ttl <= 0){
            return false;
          }
          if((resource.id === localStorage.getItem('login'))) {
            return false;
          }
          return true;
        }).map((resource) => {
          return {
            ...resource,
            name : resource.id,
            score : resource.nuggets + resource.nests,
            image : null,
          }
        });

        this.zrr =  zrrResponse;

        let promises = [];

        for (const resource of this.resources) {
          if(resource.role === "chicken" || resource.role === "cow-boy"){
            promises.push(fetchWrapper.get(`${urlImage}/${resource.name}`, body));
          }
          if(resource.role === "goldingue") {
            resource.image = goldingue;
          }
          if(resource.role === "nest") {
            resource.image = nest;
          }
          if(resource.role === "nuggets") {
            resource.image = nuggets;
          }
        }

        const allPromise = Promise.all(promises);

        try {
          const responseValues = await allPromise;

          responseValues.forEach(response => {
            let index = this.resources.findIndex(resource => resource.name === response.login);
            this.resources[index]['image'] = response.image || 'https://www.referenseo.com/wp-content/uploads/2019/03/image-attractive.jpg'
          });
        } catch (error) {
          this.isError = true;
          console.error(error);
        }
      } catch (error) {
        this.isLoading = true;
        if(error.status === 401){
          useAuthStore().logout();
        }
        throw error;
      } finally {
        console.log(this.resources)
        this.isLoading = false;
        this.isError = false;
      }
    },
    async updateResources() {
      let promisesUpdateResources = [];
      let hasCaptured = false;

      this.resources.map((resource) => {
        let distance = GeoUtils.getDistanceFromLatLonInKm(
          useUserStore().user.position.latitude,
          useUserStore().user.position.longitude,
          resource.position.latitude,
          resource.position.longitude);

        if(distance <= 5 && resource.role === "goldingue"){
          let body = {};
          if (useUserStore().user.role === "chicken") {
            body = { operationType: 'build nest', userLogin: useUserStore().user.name };
          }
          else if(useUserStore().user.role === "cow-boy") {
            body = { operationType: 'grab gold nugget', userLogin: useUserStore().user.name };
          }
          promisesUpdateResources.push(fetchWrapper.post(`${resourceURL}/api/resources/${resource.name}`, body));
          hasCaptured = true;
          if (!("Notification" in window)) {
            console.error("Ce navigateur ne supporte pas les notifications de bureau");
          } else if (Notification.permission === "granted") {
            new Notification("🐔 Space Chicken attacks!", {body : "You grab a goldingue, nice job !"});
          } else if (Notification.permission !== "denied") {
            Notification.requestPermission().then(function (permission) {
              if (permission === "granted") {
                new Notification("🐔 Space Chicken attacks!", {body : "You grab a goldingue, nice job !"});
              }
            });
          }
        }
      });
      const allPromiseReloadResources = Promise.all(promisesUpdateResources);
      try {
        await allPromiseReloadResources;
        if (hasCaptured) {
          await useUserStore().refreshUser();
        }
      } catch (error) {
        this.isError = true;
        console.error(error);
      } finally {
        await this.loadResources();
      }
    },
    updateTTL() {
      this.resources.forEach((resource) => {
        if (resource.ttl > 0){
          resource.ttl --;
        }
      });
    },
  }
});
