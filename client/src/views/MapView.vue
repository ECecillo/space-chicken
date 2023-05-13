<template>
  <section>
    <h2>Carte</h2>
    <div v-if="isLoading">Loading...</div>
    <div v-else-if="isError()">Error, please reload page or contact dev</div>
    <template v-else>
      <h4> SCORE ACTUEL : {{ getUserProfile().score }}</h4>
      <Map :markers="getMarkers()" :zrr="getZrr()" :user="getUserProfile()"/>
    </template>
  </section>
</template>

<script>
import { useResourcesStore } from '../stores/resources';
import { useUserStore } from '../stores/user';
import Map from '../components/Map.vue';

const variablePathForCurrentPlayerMock = [
  { latitude : 45.782092, longitude: 4.866461},
  { latitude : 45.781879, longitude: 4.866478},
  { latitude : 45.781640, longitude: 4.866300},
  { latitude : 45.781400, longitude: 4.865013},
  { latitude : 45.781415, longitude: 4.864520},
  { latitude : 45.781812, longitude: 4.864820},
  { latitude : 45.782115, longitude: 4.865394},
  { latitude : 45.782407, longitude: 4.865887},
  { latitude : 45.782544, longitude: 4.866438}
]; // Positions variables pour le joueur courant mockées.
export default {
  name: 'MyMap',
  components: { Map },
  data() {
    return {
      count: 0,
      interval: null,
    }
  },
  beforeMount() {
    //load user
    useUserStore().loadUser();
    //load resources
    useResourcesStore().loadResources();

    //set Intervall every 5 seconds
    this.interval = setInterval(() => {
      this.updatePosition();
      useResourcesStore().updateResources();
    }, 5000);
  },
  unmounted() {
    clearInterval(this.interval);
  },
  computed: {
    isLoading() {
      return useResourcesStore().isLoading || useUserStore().isLoading;
    },
  },
  methods: {
    getResources() {
      return useResourcesStore().resources;
    },
    getUserProfile() {
      return useUserStore().user;
    },
    isError() {
      return useResourcesStore().isError || useUserStore().isError;
    },
    getZrr() {
      return useResourcesStore().zrr;
    },
    getMarkers() {
      return this.getResources().map(resource => ({
        latitude: resource.position?.latitude,
        longitude: resource.position?.longitude,
        name: resource.name,
        image: resource.image,
        role: resource.role,
        tll: resource.ttl,
        score: resource.score,
      }));
    },
    updatePosition() {
      const position = variablePathForCurrentPlayerMock[this.count];
      useUserStore().updateUserPosition(position);
      this.count = (this.count + 1) % variablePathForCurrentPlayerMock.length; // boucle sur les positions mockées
    },
  },
}
</script>

<style scoped>
.map {
  height: 400px;
  width: 100%;
  border: 1px solid;
}
</style>
