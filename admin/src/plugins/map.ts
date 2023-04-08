// initialisation de la map
import {
    icon, LatLngBoundsLiteral, layerGroup, map as leafletMap, marker as leafletMarker, tileLayer
} from 'leaflet';

import chicken from '../img/chicken.png';
import cowboy from '../img/cowboy.png';
import goldingue from '../img/goldingue.png';
import { getResources, getZRRandTTL, setHTMLInputElementValue } from './form';

// const lat = 45.782; const lng = 4.8656; const zoom = 19;

// decalaration des icons de la carte
const icons = {
  chicken: icon({ iconUrl: chicken, iconSize: [50, 50] }),
  'cow-boy': icon({ iconUrl: cowboy, iconSize: [50, 50] }),
  goldingue: icon({ iconUrl: goldingue, iconSize: [50, 50] }),
};

// récuperation des ressources initiales du jeu
let resources = await getResources();

// récuperation des données de la zrr
const data = await getZRRandTTL();
// const ttl = data.TTL;
const zrrPoints = data.ZRR;
const pointA = zrrPoints[0];
const pointB = zrrPoints[1];
const pointC = zrrPoints[2];
const pointD = zrrPoints[3];

const corners: LatLngBoundsLiteral = [
  [pointA.latitude, pointA.longitude],
  [pointB.latitude, pointB.longitude],
  [pointC.latitude, pointC.longitude],
  [pointD.latitude, pointD.longitude],
];

const map = leafletMap('map');

// Créer une instance de LayerGroup() pour stocker les markers
const markersLayer = layerGroup().addTo(map);
const zrrLayer = layerGroup().addTo(map);

// Mise à jour de la map
function updateMap(latlng: [number, number], zoom: number) {
  // Affichage à la nouvelle position
  map.setView(latlng, zoom);
  console.log(latlng, zoom);
  // La fonction de validation du formulaire renvoie false pour bloquer le rechargement de la page.
  return false;
}

// Mise à jour du marker du zrr sur la carte
function updateMarkerZRR() {
  // Centre de la ZRR
  zrrLayer.clearLayers();
  const center = map.getCenter();
  const marker = leafletMarker([center.lat, center.lng]).addTo(map).bindPopup('Centre de la<br>ZRR.').openPopup();
  zrrLayer.addLayer(marker);
}
function createMapResources(allResources) {
  // Parcourir la liste des ressources et ajouter un marqueur pour chaque ressource
  allResources.forEach((resource) => {
    // Créer un marqueur avec l'icône correspondant au rôle de la ressource
    const marker = leafletMarker(
      [resource.position.latitude, resource.position.longitude],
      { icon: icons[resource.role] },
    )
      .addTo(map)
      .bindTooltip(resource.id, { direction: 'top' });
    markersLayer.addLayer(marker);
  });
}

async function getAndUpdateResources() {
  // Supprimer tous les markers
  markersLayer.clearLayers();

  resources = await getResources();
  createMapResources(resources);
}

// Initialisation de la map
function initMap() {
  // on place les ressources sur la map
  createMapResources(resources);

  map.fitBounds(corners);
  console.log(data);
  // Création d'un "tile layer" (permet l'affichage sur la carte)
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
  ).addTo(map);

    // On ajoute le marker de la zrr à la carte
    updateMarkerZRR()


  // Clic sur la carte
  map.on('click', (e) => {
    updateMap([e.latlng.lat, e.latlng.lng], map.getZoom());
    setHTMLInputElementValue('currentLatitude', e.latlng.lat.toString());
    setHTMLInputElementValue('currentLongitude', e.latlng.lng.toString());
    setHTMLInputElementValue('zoom', map.getZoom().toString());
  });

  const actionOnMap = ['dragend', 'zoomend'];
  actionOnMap.forEach((action) => {
    map.on(action, (e) => {
      const center = map.getCenter(); // récupère le centre de la carte après le déplacement
      console.log(e);
      setHTMLInputElementValue('currentLatitude', center.lat.toString());
      setHTMLInputElementValue('currentLongitude', center.lng.toString());
      setHTMLInputElementValue('zoom', map.getZoom().toString());
    });
  });

  setInterval(getAndUpdateResources, 3000);

  return map;
}

export { updateMap, updateMarkerZRR };
export default initMap;
