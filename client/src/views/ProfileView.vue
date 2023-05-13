<template>
  <div id="profile">
    <h1>Profile</h1>
    <div v-if="isLoading()">Loading...</div>
    <div v-else-if="isError()">No user found // Api error</div>
    <template v-else>
      <div id="informations">
        <img alt="profile image" class="profileImage" :src="loadImage()" width="125" height="125" />
        <p> <strong>Name :</strong> {{getUserProfile().name}}</p><br>
        <p> <strong>Role :</strong> {{getUserProfile().role}}</p><br>
        <p> <strong>Score :</strong> {{getUserProfile().score}}</p><br>
      </div>

      <div id="form">
        <label for="image">Changer son image [URL]: </label>
        <input type="text" id="image" name="image" v-model="image"/>
        <br />
        <label for="password">Changer / renseigner son mot de passe : </label>
        <input type="password" id="password" name="password" v-model="password"/>
        <br />
        <button id="updateUser" @click="updateUserProfile" :disabled="isDisabled">
          Valider
        </button>
      </div>
    </template>


  </div>
</template>

<style scoped>
label {
  display: inline-block;
  width: 200px;
  text-align: right;
  margin-right: 10px;
}

</style>

<script>
import { useUserStore } from '../stores/user';
export default {
  name: "Profile",
  data() {
    return {
      image: null,
      password: null,
    }
  },
  beforeMount() {
    useUserStore().loadUser();
  },
  computed: {
    isDisabled() {
      return !this.image || !this.password; //Can't update if image or password is null
    },
  },
  methods: {
    getUserProfile() {
      return useUserStore().user;
    },
    isLoading() {
      return useUserStore().isLoading;
    },
    isError() {
      return useUserStore().isError;
    },
    loadImage() {
      return this.image ? this.image : this.getUserProfile().image;
    },
    async updateUserProfile(){
      try {
        await useUserStore().updateUserProfile(this.password, this.image);
      } catch (e) {
        alert("Error while updating profile");
      } finally {
        alert("Profile updated");
        this.image = this.loadImage();
      }
    },
  },
};
</script>

