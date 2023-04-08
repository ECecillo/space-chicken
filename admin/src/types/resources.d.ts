export type Coordinate = { latitude: number; longitude: number };
export type newCoordinates = [Coordinate, Coordinate];
export type AppConfigType = {
  TTL: string;
  ZRR: [Coordinate, Coordinate, Coordinate, Coordinate];
};
/* eslint-disable no-unused-vars */
// eslint-disable-next-line no-shadow
export enum ResourceRole {
  COWBOY = 'cow-boy',
  CHICKEN = 'chicken',
  GOLDINGUE = 'goldingue',
  NEST = 'nest',
}

export type AppResourcesType = {
  id: string;
  position: Coordinate;
  role: ResourceRole;
  ttl: number;
  nuggets: number;
  nests: number;
};
