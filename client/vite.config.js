import { fileURLToPath, URL } from 'node:url'
import { VitePWA } from 'vite-plugin-pwa'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3000
  },
  plugins: [
    vue(),
    VitePWA({
      registerType: 'autoUpdate',
      manifest: {
        name: 'Space Chicken Team',
        short_name: 'App',
        description: 'Chicken 🐔 are invading the earth, get ready to defend your self !',
        start_url: '/',
        display: 'standalone',
        orientation: 'portrait'
      },
      workbox: {
        // Pre-cache tout comme ça on est sûr que l'application est disponible hors ligne.
        globPatterns: ['**/*.{js,css,html,ico,png,svg}'],
        globDirectory: 'dist/',
        skipWaiting: true, // Activate service worker when ready.
        clientsClaim: true, // Activate service worker when ready.
        runtimeCaching: [
          {
            // On part sur cette stratégie car avoir la dernière version des images
            // tout en allant demander au serveur en fond est une bonne stratégie pour ce type de besoin.
            urlPattern: /\.(?:png|jpg|jpeg|svg|gif)$/,
            handler: 'StaleWhileRevalidate',
            options: {
              cacheName: 'image-cache',
              expiration: {
                maxEntries: 20,
                maxAgeSeconds: 30 * 24 * 60 * 60 // 30 jours
              }
            }
          },
          {
            // Car le JS va très peu changer on peut partir sur cette startégie.
            urlPattern: /\.js$/,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'js-cache',
              networkTimeoutSeconds: 10 // attendez 10 secondes pour la réponse du réseau avant d'utiliser le cache
            }
          }
        ]
      },
      devOptions: {
        enabled: true
      }
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@assets': fileURLToPath(new URL('./src/assets', import.meta.url))
    }
  }
})
