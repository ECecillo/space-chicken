import { Coordinates } from '../types/resources.type';

/**
 *
 * @param {Object} coord1 {latitude, longitude} du premier point.
 * @param {Object} coord2 {latitude, longitude} du deuxième point.
 * @returns
 */
export default function haversine(coord1: Coordinates, coord2: Coordinates) {
  const { latitude: lat1, longitude: lon1 } = coord1;
  const { latitude: lat2, longitude: lon2 } = coord2;
  const R = 6371; // Earth's radius in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180; // convert to radians
  const dLon = ((lon2 - lon1) * Math.PI) / 180; // convert to radians
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const d = R * c; // distance in km
  return d;
}
