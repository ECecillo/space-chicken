<script>
export default {
  data() {
    return {
      logged: localStorage.getItem('token') ? true : false,
      loginMessage: ''
    }
  },
  methods: {
    updateData({ loggedIn, loginMessage }) {
      if (loggedIn) {
        // Mettez à jour votre variable pour indiquer que l'utilisateur est connecté
        this.loginMessage = loginMessage
        this.logged = true
      } else {
        this.loginMessage = loginMessage
      }
    },
    logout() {
      // Mettre à jour votre variable pour indiquer que l'utilisateur est déconnecté
      localStorage.removeItem('token')
      localStorage.removeItem('login')
      this.logged = false
      this.loginMessage = 'You are logged out.'
    }
  }
}
</script>

<script setup>
import { RouterLink } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import Login from './components/Login.vue'
</script>

<template>
  <div style="margin: 100px;width: 600px;">
    <header>
      <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

      <div class="wrapper">
        <nav>
          <RouterLink to="/">Home</RouterLink>
          <RouterLink to="/about">About</RouterLink>
        </nav>
      </div>
    </header>
    <br>
    <HelloWorld :msg="loginMessage" />
    <br>
    <Login v-if="!logged" :message="loginMessage" @update-data="updateData" />
    <div v-else>
      <button @click="logout">Logout</button>
    </div>
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
