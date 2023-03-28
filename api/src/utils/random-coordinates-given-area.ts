import { Coordinates } from '../types/resources.type';

export default function generateRandomCoordinate(
  coordinatesUpLeft: Coordinates,
  coordinatesDownRight: Coordinates,
): Coordinates {
  const minX = coordinatesUpLeft.longitude;
  const maxX = coordinatesDownRight.longitude;
  const minY = coordinatesUpLeft.latitude;
  const maxY = coordinatesDownRight.latitude;

  const x = Math.random() * (maxX - minX) + minX;
  const y = Math.random() * (maxY - minY) + minY;

  return { latitude: y, longitude: x };
}
