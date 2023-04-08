// Initialisation
import { updateMap } from './map.js';

const token = localStorage.getItem("token")
const apiPath = "http://localhost:3000"

// on s'assure de ne pas pouvoir entrer dans la page
if (!token) {
	window.location.href = "index.html"; // rediriger vers la page index
}

// MàJ des inputs du formulaire
function updateLatValue(lat) {
	document.getElementById("lat").value = lat;
}

function updateLonValue(lng) {
	document.getElementById("lon").value = lng;
}

function updateZoomValue(zoom) {
	document.getElementById("zoom").value = zoom;
}

function updateMapByForm(){
	const lat = document.getElementById("lat").value
	const lon = document.getElementById("lon").value
	const zoom = document.getElementById("zoom").value
	updateMap([lat,lon],zoom);
}

function setZrr(northWest, southEast) {
	document.getElementById("lat1").value = northWest.lat;
	document.getElementById("lon1").value = northWest.lng;
	document.getElementById("lat2").value = southEast.lat;
	document.getElementById("lon2").value = southEast.lng;
}

// Requêtes asynchrones
async function sendZrr() {
	// On recupere les coordonnées dans le formulaire
	const lat1 = document.getElementById("lat1").value
	const lon1 = document.getElementById("lon1").value
	const lat2 = document.getElementById("lat2").value
	const lon2 = document.getElementById("lon2").value

	if (Number.isNaN(lat1) || Number.isNaN(lon1) || Number.isNaN(lat2) || Number.isNaN(lon2)) {
		// Afficher une erreur si une des variables n'est pas un nombre à virgule flottante
		alert("Veuillez entrer des nombres pour les coordonnées.");
	}

	const headers = new Headers();
	headers.append("Content-Type", "application/json");
	headers.append("Authorization", token);
	const body = {
		"newPosition": [
			{
				"latitude": parseFloat(lat1),
				"longitude": parseFloat(lon1)
			},
			{
				"latitude": parseFloat(lat2),
				"longitude": parseFloat(lon2)
			}
		]
	};

	const requestConfig = {
		method: "PUT",
		headers,
		body: JSON.stringify(body),
		mode: "cors",
	};
	try {
		const response = await fetch(`${apiPath  }/admin/zrr`, requestConfig)
		if (response.status === 200) {
			const data = await response.json()
			console.log("mise à jour de la zrr réussi", data);
			alert("Mise à jour de la zrr réussi");
		} else {
			console.log("erreur lors de la mise à jour de la zrr", "alert-danger");
			throw new Error(`Bad response code (${  response.status  }).`);
		}
	} catch (error) {
		console.error(`In zrr: ${  error}`);
	}
}

async function setTtl() {

	const ttl = document.getElementById("ttl");

	const headers = new Headers();
	headers.append("Content-Type", "application/json");
	headers.append("Authorization", token);
	const body = {
		"ttl": ttl
	};

	const requestConfig = {
		method: "PUT",
		headers,
		body: JSON.stringify(body),
		mode: "cors",
	};
	try {
		const response = await fetch(`${apiPath  }/admin/ttl`, requestConfig)
		if (response.status === 204) {
			console.log("mise à jour du ttl réussi");
			alert("Mise à jour du ttl réussi");


		} else {
			console.log("erreur lors de la mise à jour du ttl", "alert-danger");
			throw new Error(`Bad response code (${  response.status  }).`);
		}
	} catch (error) {
		console.error(`In ttl: ${  error}`);
	}
}

function initListeners(mymap) {

	// on initialise les valeurs du formulaire de lon, lat, zoom
	const center = mymap.getCenter(); // récupère le centre de la carte après le déplacement
	updateLatValue(center.lat);
	updateLonValue(center.lng);
	updateZoomValue(mymap.getZoom());

	console.log("TODO: add more event listeners...");

	document.getElementById("lat").addEventListener("input", () => {
		updateMapByForm();
	});

	document.getElementById("lon").addEventListener("input", () => {
		updateMapByForm()
	});

	document.getElementById("zoom").addEventListener("input", () => {
		updateMapByForm()
	});

	document.getElementById("setZrrButton").addEventListener("click", () => {
		// Récupère les coordonnées des coins supérieurs gauche et inférieur droit de la carte
		const bounds = mymap.getBounds();
		const northWest = bounds.getNorthWest(); // coin supérieur gauche
		const southEast = bounds.getSouthEast(); // coin inférieur droit
		console.log('Coordonnées du coin supérieur gauche:', northWest);
		console.log('Coordonnées du coin inférieur droit:', southEast);

		setZrr(northWest, southEast);
	});

	document.getElementById("sendZrrButton").addEventListener("click", () => {
		sendZrr();
	});

	document.getElementById("setTtlButton").addEventListener("click", () => {
		setTtl();
	});
}

async function getZRRandTTL() {
	const headers = new Headers();
	headers.append("Content-Type", "application/json");
	headers.append("Authorization", token);
	const requestConfig = {
		method: "GET",
		headers,
		mode : "cors"
	};
	try {
		const response = await fetch(`${apiPath  }/admin/`, requestConfig)
		if (response.status === 200) {
			const data = await response.json()
			console.log("récupération de la zrr réussi", data.ZRR);
			return data;
		} 
			console.log("erreur lors de la mise à jour de la zrr", "alert-danger");
			throw new Error(`Bad response code (${  response.status  }).`);
		
	} catch (error) {
		console.error(`In getZRRandTTL: ${  error}`);
	}
}
async function getResources() {
	const headers = new Headers();
	headers.append("Content-Type", "application/json");
	headers.append("Authorization", token);
	const requestConfig = {
		method: "GET",
		headers,
		mode : "cors"
	};
	try {
		const response = await fetch(`${apiPath  }/api/resources/`, requestConfig)
		if (response.status === 200) {
			const data = await response.json()
			console.log("récupération des ressources réussi", data);
			return data;
		}
		if (response.status === 401) {
			alert("vous allez etre déconnecté");
			window.location.href = "index.html"; // rediriger vers la page index
		}
			console.log("erreur lors de la mise à jour de la zrr", "alert-danger");
			throw new Error(`Bad response code (${  response.status  }).`);
		
	} catch (error) {
		console.error(`In getresources: ${  error}`);
	}
}

export { updateLatValue, updateLonValue, updateZoomValue, getZRRandTTL, getResources };
export default initListeners;