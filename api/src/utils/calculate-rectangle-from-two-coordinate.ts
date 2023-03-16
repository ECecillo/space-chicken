import { Coordinates } from '../types/resources.type';

// A------------------B
/* |                  |
   |                  |
   D------------------C
*/
// No matter of NorthWest/SouthEast or NorthEast/SouthWest given,
// we return the 4 coordinates of the rectangle.
export default function getRectangleGPSPoints(
  coordinateCornerA: Coordinates,
  coordinateCornerC: Coordinates,
): [Coordinates, Coordinates, Coordinates, Coordinates] {
  const coordinateCornerB: Coordinates = {
    latitude: coordinateCornerA.latitude,
    longitude: coordinateCornerC.longitude,
  };
  const coordinateCornerD: Coordinates = {
    latitude: coordinateCornerC.latitude,
    longitude: coordinateCornerA.longitude,
  };

  return [
    coordinateCornerA,
    coordinateCornerC,
    coordinateCornerB,
    coordinateCornerD,
  ];
}
