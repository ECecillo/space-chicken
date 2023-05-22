import { Map } from 'leaflet';

import { getAppConfig } from '../get-app-config.ts';
import { getAppResources } from '../get-app-resources.ts';
import { Coordinate, newCoordinates } from '../types/resources';
import { updateGoldingueRequest } from '../update-one-goldingue-request';
import { updateTTLRequest } from '../update-ttl-request.ts';
import { updateZRRRequest } from '../update-zrr-request.ts';
import {
    getHTMLInputElementValue, setHTMLInputElementValue
} from '../utils/get-html-element-value.ts';
// Initialisation
import { updateMap, updateMarkerZRR } from './map.ts';

const token = localStorage.getItem('token');

// On s'assure de ne pas pouvoir entrer dans la page sans être connecté.
if (!token) window.location.href = 'index.html'; // rediriger vers la page index

// Mise à jour de la carte par les inputs du formulaire.
function updateMapByForm() {
  const currentLatitude = getHTMLInputElementValue('currentLatitude');
  const currentLongitude = getHTMLInputElementValue('currentLongitude');
  const zoom = getHTMLInputElementValue('zoom');
  updateMap([Number(currentLatitude), Number(currentLongitude)], Number(zoom));
}

function setGolingue(latitude, longitude) {
  setHTMLInputElementValue('goldingueLatitude', latitude);
  setHTMLInputElementValue('goldingueLongitude', longitude);
}
function setZrr(northWest, southEast) {
  setHTMLInputElementValue('northWestLatitude', northWest.lat);
  setHTMLInputElementValue('northWestLongitude', northWest.lng);
  setHTMLInputElementValue('southEastLatitude', southEast.lat);
  setHTMLInputElementValue('southEastLongitude', southEast.lng);
}

// Requêtes asynchrones
async function sendZrr() {
  // On recupere les coordonnées dans le formulaire
  const northWestLatitude = getHTMLInputElementValue('northWestLatitude');
  const northWestLongitude = getHTMLInputElementValue('northWestLongitude');
  const southEastLatitude = getHTMLInputElementValue('southEastLatitude');
  const southEastLongitude = getHTMLInputElementValue('southEastLongitude');

  if (
    Number.isNaN(northWestLatitude) ||
    Number.isNaN(northWestLongitude) ||
    Number.isNaN(southEastLatitude) ||
    Number.isNaN(southEastLongitude)
  ) {
    // Afficher une erreur si une des variables n'est pas un nombre à virgule flottante
    alert('Veuillez entrer des nombres pour les coordonnées.');
  } else if (
    southEastLongitude === '' ||
    southEastLatitude === '' ||
    northWestLongitude === '' ||
    northWestLatitude === ''
  ) {
    alert('Vous devez entrer des coordonnées pour la zrr');
  } else {
    const positionPayload: newCoordinates = [
      {
        latitude: parseFloat(northWestLatitude),
        longitude: parseFloat(northWestLongitude),
      },
      {
        latitude: parseFloat(southEastLatitude),
        longitude: parseFloat(southEastLongitude),
      },
    ];
    try {
      await updateZRRRequest({ token }, { newPosition: positionPayload });
      updateMarkerZRR();
    } catch (error) {
      console.log(
        `erreur lors de la mise à jour de la zrr ${error}`,
        'alert-danger',
      );
    }
  }
}

async function sendGoldingue() {
  const goldingueLatitude = getHTMLInputElementValue('goldingueLatitude');
  const goldingueLongitude = getHTMLInputElementValue('goldingueLongitude');

  if (Number.isNaN(goldingueLatitude) || Number.isNaN(goldingueLongitude)) {
    // Afficher une erreur si une des variables n'est pas un nombre à virgule flottante
    alert('Veuillez entrer des nombres pour les coordonnées.');
  } else if (goldingueLatitude === '' || goldingueLongitude === '')
    alert('Vous devez entrer des coordonnées pour la goldingue');
  else {
    const position: Coordinate = {
      latitude: parseFloat(goldingueLatitude),
      longitude: parseFloat(goldingueLongitude),
    };
    try {
      await updateGoldingueRequest({ token }, position);
    } catch (error) {
      console.log(
        `erreur lors de la création du goldingue ${error}`,
        'alert-danger',
      );
    }
  }
}

async function setTtl() {
  const ttl = getHTMLInputElementValue('ttl');

  try {
    await updateTTLRequest({ token }, ttl);
  } catch (error) {
    console.log(`erreur lors de la mise à jour du ttl ${error}`, 'alert-danger');
  }
}

function initListeners(map: Map) {
  // on initialise les valeurs du formulaire de lon, lat, zoom
  const center = map.getCenter(); // récupère le centre de la carte après le déplacement
  setHTMLInputElementValue('currentLatitude', center.lat.toString());
  setHTMLInputElementValue('currentLongitude', center.lng.toString());
  setHTMLInputElementValue('zoom', map.getZoom().toString());

  document
    .getElementById('currentLatitude')
    .addEventListener('input', () => updateMapByForm());

  document
    .getElementById('currentLongitude')
    .addEventListener('input', () => updateMapByForm());

  document.getElementById('zoom').addEventListener('input', () => updateMapByForm());

  document.getElementById('setZrrButton').addEventListener('click', () => {
    // Récupère les coordonnées des coins supérieurs gauche et inférieur droit de la carte
    const bounds = map.getBounds();
    const northWest = bounds.getNorthWest(); // coin supérieur gauche
    const southEast = bounds.getSouthEast(); // coin inférieur droit
    console.log('Coordonnées du coin supérieur gauche:', northWest);
    console.log('Coordonnées du coin inférieur droit:', southEast);

    setZrr(northWest, southEast);
  });

  document.getElementById('setGoldingueButton').addEventListener('click', () => {
    // Récupère les coordonnées du click sur la carte
    const click = map.getCenter();

    setGolingue(click.lat, click.lng);
  });
  document.getElementById('setTtlButton').addEventListener('click', () => setTtl());

  document
    .getElementById('sendZrrButton')
    .addEventListener('click', () => sendZrr());

  document
    .getElementById('sendOneGoldingueButton')
    .addEventListener('click', () => sendGoldingue());
}

async function getZRRandTTL() {
  try {
    const data = await getAppConfig({ token });
    console.log('récupération de la config réussi', data);
    setHTMLInputElementValue('ttl', data.TTL);
    return data;
  } catch (error) {
    console.log(
      `erreur lors de la récupération des données de configuration de l'application ${error}`,
      'alert-danger',
    );
  }
}
async function getResources() {
  try {
    const data = await getAppResources({ token });
    console.log('récupération des ressources réussi', data);
    return data;
  } catch (error) {
    console.log(
      `erreur lors de la récupération des ressources de l'application ${error}`,
      'alert-danger',
    );
  }
}

export { getZRRandTTL, getResources, setHTMLInputElementValue };
export default initListeners;
