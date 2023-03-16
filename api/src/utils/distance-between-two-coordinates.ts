import { Coordinates } from '../types/resources.type';

/**
 *
 * @param {Array} coordinateOne [latitude, longitude] du premier point.
 * @param {Array} coordinateTwo [latitude, longitude] du deuxième point.
 * @returns
 */
export default function distanceBetweenTwoCoordinates(
  coordinateOne: Coordinates,
  coordinateTwo: Coordinates,
) {
  const { latitude: x1, longitude: y1 } = coordinateOne;
  const { latitude: x2, longitude: y2 } = coordinateTwo;
  const dx = x2 - x1;
  const dy = y2 - y1;
  return Math.sqrt(dx * dx + dy * dy);
}
