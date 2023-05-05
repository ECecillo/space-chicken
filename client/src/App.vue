<script>
</script>

<script setup>
import { RouterLink, RouterView } from 'vue-router';
import { useAuthStore } from './stores/authentification';

const authStore = useAuthStore();
const user = authStore.user;
</script>

<template>
  <div style="margin: 100px;width: 600px;">
    <header>
      <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

      <div id="nav" class="wrapper">
        <nav v-show="user" >
          <RouterLink to="/" class="nav-item nav-link">Home</RouterLink>
          <RouterLink to="/map" class="nav-item nav-link">My map</RouterLink>
          <RouterLink to="/profile" class="nav-item nav-link">Profile</RouterLink>
          <a @click="authStore.logout()" class="nav-item nav-link">Logout</a>
        </nav>
      </div>
    </header>

    <main id="content">
      <router-view />
    </main>

  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
