import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/authentification';
import HomeView from '../views/HomeView.vue';
import MapView from '../views/MapView.vue';
import LoginView from '../views/LoginView.vue';
import ProfileView from '../views/ProfileView.vue';
import GeolocView from '../views/GeolocView.vue'

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  linkActiveClass: 'active',
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/map',
      name: 'map',
      component: MapView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView
    },
    {
      path: '/geoloc',
      name: 'geoloc',
      component: GeolocView

    }
  ]
});

router.beforeEach(async (to) => {
  // redirect to login page if not log or unauthorized.
  const publicPages = ['/login'];
  const authRequired = !publicPages.includes(to.path);
  const auth = useAuthStore();
  if (authRequired && auth.user.login === null) {
    auth.returnUrl = to.fullPath;
    return '/login';
  }
});

