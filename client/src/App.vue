<script>
</script>

<script setup>
import { RouterView } from 'vue-router';
import Navigation from './components/Navigation.vue';
import { useAuthStore } from './stores/authentification';
function getUserLogin() {
  return useAuthStore().user?.login;
}
</script>

<template>
  <div style="margin: 100px;width: 600px;">
    <Navigation :user="getUserLogin()" />
    <main id="content">
      <router-view />
    </main>
  </div>
</template>

