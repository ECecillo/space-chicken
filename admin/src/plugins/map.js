import L from "leaflet";
// initialisation de la map
import { updateLatValue, updateLonValue, updateZoomValue, getZRRandTTL, getResources } from './form.js';
import chicken from '../img/chicken.png';
import cowboy from '../img/cowboy.png';
import goldingue from '../img/goldingue.png';

// const lat = 45.782; const lng = 4.8656; const zoom = 19;

// decalaration des icons de la carte
const icons = {
	"chicken": L.icon({ iconUrl: chicken, iconSize: [50, 50] }),
	"cow-boy": L.icon({ iconUrl: cowboy, iconSize: [50, 50] }),
	"goldingue": L.icon({ iconUrl: goldingue, iconSize: [50, 50] })
};

// récuperation des ressources initiales du jeu
let resources = await getResources()

// récuperation des données de la zrr
const data = await getZRRandTTL();
// const ttl = data.TTL;
const zrrPoints = data.ZRR;
const pointA = zrrPoints[0];
const pointB = zrrPoints[1];
const pointC = zrrPoints[2];
const pointD = zrrPoints[3];

const corners = [
	[pointA.latitude, pointA.longitude],
	[pointB.latitude, pointB.longitude],
	[pointC.latitude, pointC.longitude],
	[pointD.latitude, pointD.longitude] ];

const map = L.map('map')

// Créer une instance de L.LayerGroup() pour stocker les markers
const markersLayer = L.layerGroup().addTo(map);


// Mise à jour de la map
function updateMap(latlng, zoom) {
	// Affichage à la nouvelle position
	map.setView(latlng, zoom);
	console.log(latlng,zoom);
	// La fonction de validation du formulaire renvoie false pour bloquer le rechargement de la page.
	return false;
}

function createMapResources(allResources) {
	// Parcourir la liste des ressources et ajouter un marqueur pour chaque ressource
	allResources.forEach((resource) => {
		// Créer un marqueur avec l'icône correspondant au rôle de la ressource
		const marker = L.marker([resource.position.latitude, resource.position.longitude], { icon: icons[resource.role] })
			.addTo(map).bindTooltip(resource.id, { direction: 'top' });
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

	 map.fitBounds(corners)
	 console.log(data)
	// Création d'un "tile layer" (permet l'affichage sur la carte)
	L.tileLayer('https://api.mapbox.com/v4/mapbox.satellite/{z}/{x}/{y}@2x.jpg90?access_token=pk.eyJ1IjoieGFkZXMxMDExNCIsImEiOiJjbGZoZTFvbTYwM29sM3ByMGo3Z3Mya3dhIn0.df9VnZ0zo7sdcqGNbfrAzQ', {
		maxZoom: 21,
		minZoom: 1,
		attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
			'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
		id: 'mapbox/streets-v11',
		tileSize: 512,
		zoomOffset: -1,
		accessToken: 'pk.eyJ1IjoibTFpZjEzIiwiYSI6ImNqczBubmhyajFnMnY0YWx4c2FwMmRtbm4ifQ.O6W7HeTW3UvOVgjCiPrdsA'
	}).addTo(map);


	 // Centre de la ZRR
	let center = map.getCenter();

	// Clic sur la carte
	map.on('click', e => {
		updateMap([e.latlng.lat, e.latlng.lng], map.getZoom());
		updateLatValue(e.latlng.lat);
		updateLonValue(e.latlng.lng);
		updateZoomValue(map.getZoom());
	});

	map.on('dragend', () => {
		center = map.getCenter(); // récupère le centre de la carte après le déplacement
		console.log('La carte a été déplacée. Nouveau centre:', center);
		updateMap([center.lat, center.lng], map.getZoom())
		updateLatValue(center.lat);
		updateLonValue(center.lng);
		updateZoomValue(map.getZoom());
		console.log("coord",map.getBounds())

	});
	map.on('zoomend', e => {
		center = map.getCenter(); // récupère le centre de la carte après le déplacement
		console.log(e)
		updateLatValue(center.lat);
		updateLonValue(center.lng);
		updateZoomValue(map.getZoom());
	});
	console.log("coord",map.getCenter())

	setInterval(getAndUpdateResources, 3000);

	return map;
}

export { updateMap };
export default initMap;