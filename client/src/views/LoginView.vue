<script setup>
import { Form, Field } from 'vee-validate';
import * as Yup from 'yup';

import { useAuthStore } from '../stores/authentification';

const schema = Yup.object().shape({
  username: Yup.string().required('Login is required'),
  password: Yup.string().required('Password is required')
});

function onSubmit(values, { setErrors }) {
  const authStore = useAuthStore();
  const { username, password } = values;

  return authStore.login(username, password)
      .catch(error => setErrors({ apiError: error }));
}
</script>

<template>
  <div>
    <h1 id="title">Login</h1>
    <Form @submit="onSubmit" :validation-schema="schema" v-slot="{ errors, isSubmitting }">
      <div class="form-group">
        <label>Username</label>
        <Field name="username" type="text" class="form-control" :class="{ 'is-invalid': errors.username }" />
      </div>
      <div class="form-group">
        <label>Password</label>
        <Field name="password" type="password" class="form-control" :class="{ 'is-invalid': errors.password }" />
      </div>
      <div class="form-group">
        <button class="btn btn-primary" :disabled="isSubmitting">
          <span v-show="isSubmitting"></span>
          Login
        </button>
      </div>
      <div id="error-messages">
        <div class="invalid-feedback">{{errors.username}}</div>
        <div class="invalid-feedback">{{errors.password}}</div>
        <div v-if="errors.apiError" class="alert-danger">{{errors.apiError}}</div>
      </div>

    </Form>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  top: -10px;
}
.form-group {
  margin-bottom: 1rem;
}
label {
  margin-right: 0.5rem;
  color: #2f97f8;
}
.invalid-feedback, .alert-danger {
  margin-top: 1rem;
  color: red;
}
</style>
