<template>
  <section>
    <h2>Carte</h2>
    <div v-if="isLoading">Loading...</div>
    <div v-else-if="isError()">Error, please reload page or contact dev</div>
    <template v-else>
      <h4>SCORE ACTUEL : {{ getUserProfile().score }}</h4>
      <Map :resources="getResourcesForMarkers()" :zrr="getZrr()" :user="getUserProfile()" />
      <VibrationView :show="showGoldingModal" @close="closeGoldingModal" />
    </template>
    <p class="content">
      <span v-if="geolocation" style="color: green"
        >Votre navigateur supporte la géolocalisation</span
      >
      <span v-else style="color: red"
        >Votre navigateur ne supporte pas la géolocalisation. <br /><strong
          >Il est impossible d'utiliser l'application...</strong
        ></span
      >
    </p>
  </section>
</template>

<script>
import { useResourcesStore } from '../stores/resources';
import { useUserStore } from '../stores/user';
import { showAppText } from '../utils/special-log';
import Map from '../components/Map.vue';
import VibrationView from "@/views/VibrationView.vue";

export default {
  name: 'MyMap',
  components: { Map, VibrationView },
  data() {
    return {
      count: 0,
      interval: null,
      intervalSecond: null,
      geolocation: navigator.geolocation,
      showGoldingModal: false,
      oldScore: null,
    }
  },
  beforeMount() {
    // For dope.
    showAppText();
    //load user
    useUserStore()
      .loadUser()
      .then(() => {
        // get user position with service worker
        // and update his position in store on sucess.
        this.getLocation();
      });
    //load resources
    useResourcesStore().loadResources();

    //set Intervall every 5 seconds
    this.interval = setInterval(() => {
      useResourcesStore().updateResources();
    }, 5000);

    //set Intervall every 1 seconds
    this.intervalSecond = setInterval(() => {
      console.log('update ttl');
      useResourcesStore().updateTTL();
    }, 1000);
  },
  unmounted() {
    clearInterval(this.interval);
    clearInterval(this.intervalSecond);
    navigator.geolocation.clearWatch(this.watchId);
    navigator.vibrate(0);
  },
  computed: {
    isLoading() {
      return useResourcesStore().isLoading || useUserStore().isLoading;
    },
    userProfileScore() {
      return this.getUserProfile().score;
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
    getResourcesForMarkers() {
      return this.getResources().map((resource) => ({
        latitude: resource.position?.latitude,
        longitude: resource.position?.longitude,
        name: resource.name,
        image: resource.image,
        role: resource.role,
        tll: resource.ttl,
        score: resource.score
      }));
    },
    getLocation() {
      if ('geolocation' in navigator) {
        const options = {
          timeout: 60000 // Délai d'attente d'une minute
        };

        navigator.geolocation.watchPosition(this.updatePosition, this.positionError, options);
      } else {
        this.message = "La Geolocation API n'est pas disponible sur votre navigateur.";
      }
    },
    positionError(error) {
      if (error.code === error.TIMEOUT) {
        console.log('La récupération de la position a pris trop de temps. Veuillez réessayer.');
      } else {
        console.log('Erreur lors de la récupération de la position : ' + error.message);
      }
    },
    updatePosition(position) {
      // const position = variablePathForCurrentPlayerMock[this.count] //Todo : simulation déplacement joueur.
      console.log('Position obtenu.');
      // this.positionObtained = true
      console.log(`Coordonnées de ${useUserStore().user.name} \n`, position.coords);
      const { latitude, longitude } = position.coords;
      useUserStore().updateUserPosition({ latitude, longitude });
      // this.count = (this.count + 1) % variablePathForCurrentPlayerMock.length // boucle sur les positions mockées
    },
    closeGoldingModal() {
      this.showGoldingModal = false;
      navigator.vibrate(0); // arrête la vibration
    },
    triggerVibration() {
      if ("vibrate" in navigator) {
        // durée de la vibration en millisecondes
        navigator.vibrate(500);
        console.log("VIBRATION !!!!")
      } else {
        console.log("Vibration API not supported");
      }
    },
  },
  watch: {
    userProfileScore(newValue, oldValue) {
      if (this.oldScore !== null && newValue > oldValue) {
        console.log("Le score a augmenté");
        this.showGoldingModal = true;
      }
      this.oldScore = newValue;
    },
    showGoldingModal(newValue) {
      if (newValue) {
        this.triggerVibration();
      }
    },
  },
};
</script>

<style scoped>
.map {
  height: 400px;
  width: 100%;
  border: 1px solid;
}
</style>
