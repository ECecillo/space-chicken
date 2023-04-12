<template>
    <h2>{{ message }}</h2>
    <label for="login">Login :&nbsp;</label>
    <input type="text" name="login" id="login" />
    <br />
    <label for="password">Password :&nbsp;</label>
    <input type="password" name="password" id="password" />
    <br />
    <button @click="$emit('loginEvent')">Send</button>

</template>

<script>
export default {
  name: "Login",
  props: {
    message: String
  },
  methods: {
    login: async () => {
      try {
      // Ici, vous devez appeler la fonction de connexion que vous avez écrite précédemment et passer les identifiants
      await login(this.userLogin, this.userPassword);
      this.$emit("update-data", { loggedIn: true, loginMessage: "Connexion réussie !" });
    } catch (error) {
      this.$emit("update-data", { loggedIn: false, errorMessage: "Erreur de connexion" });
    }
    },
  },
  emits: ['loginEvent']
};
</script>

<style scoped>
input,
input[type="submit"],
select {
  color: grey;
  border: 1px solid;
}
</style>
