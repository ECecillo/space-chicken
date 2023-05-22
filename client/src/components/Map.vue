<template>
  <div id="map" class="map" ref="map"></div>
</template>

<script>
import {
  layerGroup, icon, map as leafletMap, Popup, DomUtil, marker, Marker, tileLayer, popup, polygon
} from 'leaflet';
import 'leaflet/dist/leaflet.css';
import chicken from '@assets/icons/chicken.png';
import cowboy from '@assets/icons/cowboy.png';
import goldingue from '@assets/icons/goldingue.png';
import nest from '@assets/icons/nest.png';
import nuggets from '@assets/icons/nuggets.png';
import userImage from '@assets/icons/user.png';
import GeoUtils from '../utils/geoUtils';
import { useUserStore } from '../stores/user';

const icons = {
  'chicken': icon({ iconUrl: chicken, iconSize: [30, 30] }),
  'cow-boy': icon({ iconUrl: cowboy, iconSize: [30, 30] }),
  'goldingue': icon({ iconUrl: goldingue, iconSize: [30, 30] }),
  'nest': icon({ iconUrl: nest, iconSize: [30, 30] }),
  'nuggets': icon({ iconUrl: nuggets, iconSize: [30, 30] }),
};

export default {
  name: "Map",
  props: {
    resources: {
      type: Array,
      required: true
    },
    zrr: {
      type: Array,
      required: true
    },
    user: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      lat: 45.782,
      lng: 4.8656,
      zoom: 19,
      map: null,
      markersLayer: null,
      currentMarkerConsulted: null,
    }
  },
  methods: {
    // Charge les makers sur la map
    addMarker: function (latitudeMarker, longitudeMaker, name, image, icon, score, ttl, role) {
      const dist = GeoUtils.getDistanceFromLatLonInKm(
          useUserStore().user.position.latitude,
          useUserStore().user.position.longitude,
          latitudeMarker,
          longitudeMaker,
      );// Récupère la distance entre le joueur courant et la ressource pour l'afficher dans le popup.
      const Onemarker = marker(
        [latitudeMarker, longitudeMaker],
        {
          icon: icon,
          title: name,
        }).addTo(this.map)

      if(role === "goldingue" ) {
        Onemarker.bindPopup(`
          <img src="${image}" alt="${name}" width="30" height="30"><div>Role : ${role}</div><div>TLL : ${ttl}</div>
          <div>Name = ${name}<br> Distance = ${dist} mètres</div><br>Score = ${score}`);
      } else {
        Onemarker.bindPopup(`
          <img src="${image}" alt="${name}" width="30" height="30"><div>Role : ${role}</div>
          <div>Name = ${name}<br> Distance = ${dist} mètres</div><br>Score = ${score}`);
      }

      this.markersLayer.addLayer(Onemarker);
      return Onemarker;
    },
  },
  mounted() {
    console.log('mounted');
    // Création de la carte
    this.map = leafletMap('map');
    this.markersLayer = layerGroup().addTo(this.map);

    this.map.setView([this.lat, this.lng], this.zoom);
    tileLayer(
        'https://api.mapbox.com/v4/mapbox.satellite/{z}/{x}/{y}@2x.jpg90?access_token=pk.eyJ1IjoieGFkZXMxMDExNCIsImEiOiJjbGZoZTFvbTYwM29sM3ByMGo3Z3Mya3dhIn0.df9VnZ0zo7sdcqGNbfrAzQ',
        {
          maxZoom: 21,
          minZoom: 1,
          attribution:
              'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
              '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
              'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
          id: 'mapbox/streets-v11',
          tileSize: 512,
          zoomOffset: -1,
          accessToken:
              'pk.eyJ1IjoibTFpZjEzIiwiYSI6ImNqczBubmhyajFnMnY0YWx4c2FwMmRtbm4ifQ.O6W7HeTW3UvOVgjCiPrdsA',
        },
    ).addTo(this.map);

    //zoom animate debug
    Popup.prototype._animateZoom = function (e) {
      if (!this._map) {
        return
      }
      let pos = this._map._latLngToNewLayerPoint(this._latlng, e.zoom, e.center),
          anchor = this._getAnchor()
      DomUtil.setPosition(this._container, pos.add(anchor))
    }

    // Clic sur la carte
    this.map.on('click', (e) => {
      this.lat = e.latlng.lat;
      this.lng = e.latlng.lng;
      this.currentMarkerConsulted = null;
    });

    // Ajout des markers
    this.resources.map(resource => {
      this.addMarker(
        resource.latitude,
        resource.longitude,
        resource.name,
        resource.image? resource.image : icons[resource.role],
          icons[resource.role],
        resource.score,
        resource.tll,
        resource.role,
      );
    });

    // Ajout du joueur courant
    this.addMarker(
        this.user.position.latitude,
        this.user.position.longitude,
        this.user.name,
        this.user.image? this.user.image : icons[this.user.role],
        icons[this.user.role],
        this.user.score,
        this.user.tll,
        this.user.role,
    );

    // Ajout de la ZRR
    let latlngs = [];
    this.zrr.map( (corner) => {
        latlngs.push([corner.latitude, corner.longitude]);
    });
    let polygonZrr = polygon(latlngs, {color: 'red'});
    this.markersLayer.addLayer(polygonZrr);
    this.map.fitBounds(polygonZrr.getBounds());

    // Listener sur la popup Event pour la réafficher
    this.map.on('popupopen', (e) => {
      this.currentMarkerConsulted = e.popup._source.options.title; // Récupère le nom du marker affiché
    });
  },
  updated() {
    // Suppression des markers clear map
    this.markersLayer.clearLayers();

    // Réajout du marker du joueur
    const marker = this.addMarker(
        this.user.position.latitude,
        this.user.position.longitude,
        this.user.name,
        this.user.image? this.user.image : icons[this.user.role],
          icons[this.user.role],
        this.user.score,
        this.user.tll,
        this.user.role,
    );
    if (this.currentMarkerConsulted === marker.options.title){
      marker.openPopup();
    }


    // Réajout des markers actualisés
    this.resources.forEach(resource => {
      const marker = this.addMarker(
        resource.latitude,
        resource.longitude,
        resource.name,
        resource.image? resource.image : icons[resource.role],
          icons[resource.role],
        resource.score,
        resource.tll,
        resource.role,
      );
      if (this.currentMarkerConsulted === marker.options.title){
        marker.openPopup();
      }
    });

    // Réajout de la ZRR
    let latlngs = [];
    this.zrr.map( (corner) => {
        latlngs.push([corner.latitude, corner.longitude]);
    });
    let polygonZrr = polygon(latlngs, {color: 'red'});
    this.markersLayer.addLayer(polygonZrr);
  }
}
</script>

<style scoped>

</style>
