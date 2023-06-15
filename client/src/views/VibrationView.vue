<template>
  <div v-if="show" class="modal">
    <div class="modal-content">
      <h2>Bravo !</h2>
      <img class="modal-image" src="@assets/icons/goldingue.png">
      <p>Vous avez attrapé un plant de goldingue !</p>
      <button @click="$emit('close')">Fermer</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GoldingModal',
  props: {
    show: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;

}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
  color: #181818;
}


.modal-image {
  max-width: 50px;  /* Largeur maximale de l'image */
  max-height: 50px; /* Hauteur maximale de l'image */
}</style>