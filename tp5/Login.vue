<template>
  <form id="loginForm">
    <label for="login">Login :</label>
    <input type="text" name="login" id="login" />
    <br />
    <label for="password">Password :</label>
    <input type="password" name="password" id="password" />
    <br />
    <button v-on:click="login">Send</button>
  </form>
</template>

<script>
export default {
  name: "Login",
  methods: {
    login: () => {
      console.log("Login cliqué.");
      //TODO qqch ici
    },
  },
};
</script>

<style scoped>
input,
input[type="submit"],
select {
  background-color: #2f4f4f !important;
  color: lightgray;
  border: 1px solid;
}
</style>
