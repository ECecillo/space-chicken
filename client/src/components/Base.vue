
<template>
  <div class="greetings">
    <h1 class="green">{{ msg }}</h1>
    <h3>
      Welcome to the Space chicken project, <br>powered by
      <a href="https://vitejs.dev/" target="_blank" rel="noopener">Vite</a> +
      <a href="https://vuejs.org/" target="_blank" rel="noopener">Vue 3</a> and
      <a href="https://pinia.vuejs.org/" target="_blank" rel="noopener">Pinia</a>
    </h3>
  </div>
</template>

<script>
export default {
  name: 'Base',
  props: {
    msg: {
      type: String,
      required: true
    }
  }
}

</script>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
