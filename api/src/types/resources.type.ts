/* eslint-disable no-unused-vars */
// eslint-disable-next-line no-shadow
export enum ResourceRole {
  COWBOY = 'cow-boy',
  CHICKEN = 'chicken',
  GOLDINGUE = 'goldingue',
  NEST = 'nest',
}

export type Coordinates = {
  latitude: number;
  longitude: number;
};

export type Resource = {
  id: string;
  position: Coordinates;
  role: ResourceRole; // 'cow-boy' | 'chicken' | 'goldingue' | 'nest'
  ttl: number;
  nuggets: number;
  nests: number;
};

export type ResourcesStore = Resource[];