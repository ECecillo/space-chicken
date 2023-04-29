<template>
  <!-- <h2>{{ message }}</h2> -->

  <label for="login">Login :&nbsp;</label>
  <br />
  <input type="text" name="login" id="login" v-model="userLogin" />
  <br />
  <label for="password">Password :&nbsp;</label>
  <br />
  <input type="password" name="password" id="password" v-model="userPassword" />
  <br />
  <button @click="login">Send</button>
</template>

<script>
export default {
  name: 'Login',
  props: {
    message: String
  },
  data() {
    return {
      userLogin: '',
      userPassword: ''
    }
  },
  methods: {
    async login() {
      try {
        const response = await userAuthentification(this.userLogin, this.userPassword)
        const bearer = response.headers.get('Authorization')
        if (!bearer) throw new Error('No token found.')
        localStorage.setItem('login', this.userLogin)
        localStorage.setItem('token', bearer)
        this.$emit('update-data', { loggedIn: true, loginMessage: 'You are logged in.' })
      } catch (error) {
        this.$emit('update-data', {
          loggedIn: false,
          loginMessage: 'Erreur de connexion ' + error + '.'
        })
      }
    }
  },
  emits: ['updateData']
}
</script>

<script setup>
import { login as userAuthentification } from '../api/server/user/login.js'
</script>

<style scoped>
input,
input[type='submit'],
select {
  color: grey;
  border: 1px solid;
}
</style>
