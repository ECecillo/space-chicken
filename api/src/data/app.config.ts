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
      latitude: 45.782244, // A (y1, x1)
      longitude: 4.864863,
    },
    {
      latitude: 45.782641, // B (y3, x1)
      longitude: 4.864863,
    },
    {
      latitude: 45.782641, // C (y3, x3)
      longitude: 4.864884,
    },
    {
      latitude: 45.782244, // D (y1, x3)
      longitude: 4.864884,
    },
    // A------------------B
    /* |                  |
       |                  |
       D------------------C
    */
  ],
};
