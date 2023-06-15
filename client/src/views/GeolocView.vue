<template>
  <div>
    <p>{{ message }}</p>
    <div v-if="positionObtained">
      <p>Latitude : {{ latitude }}</p>
      <p>Longitude : {{ longitude }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: "Récupération de la position en cours...",
      positionObtained: false,
      latitude: null,
      longitude: null,
    };
  },
  mounted() {
    this.getLocation();
  },
  methods: {
    getLocation() {
      if ("geolocation" in navigator) {
        const options = {
          timeout: 60000, // Délai d'attente d'une minute
        };

        navigator.geolocation.watchPosition(
          this.positionUpdate,
          this.positionError,
          options
        );
      } else {
        this.message = "La Geolocation API n'est pas disponible sur votre navigateur.";
      }
    },
    positionUpdate(position) {
      this.message = "Position obtenue.";
      this.positionObtained = true;
      this.latitude = position.coords.latitude;
      this.longitude = position.coords.longitude;

    },
    positionError(error) {
      if (error.code === error.TIMEOUT) {
        this.message = "La récupération de la position a pris trop de temps. Veuillez réessayer.";
      } else {
        this.message = "Erreur lors de la récupération de la position : " + error.message;
      }
    },
  },
};
</script>
