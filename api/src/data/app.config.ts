import { Coordinates } from '../types/resources.type';

export type ConfigType = {
  TTL: number;
  ZRR: Coordinates[];
};

export const config: ConfigType = {
  TTL: 60, // 1 minute.
  // NAUTIBUS
  ZRR: [
    {
      latitude: 45.78237323836341, // A (y1, x1)
      longitude: 4.8676273226737985,
    },
    {
      latitude: 45.781625019739884, // B (y3, x1)
      longitude: 4.8676273226737985,
    },
    {
      latitude: 45.781625019739884, // C (y3, x3)
      longitude: 4.863571822643281,
    },
    {
      latitude: 45.78237323836341, // D (y1, x3)
      longitude: 4.863571822643281,
    },
    // A------------------B
    /* |                  |
       |                  |
       D------------------C
    */
  ],
};
