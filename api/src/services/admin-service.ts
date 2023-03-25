import { config } from '../data/app.config';
import { Coordinates } from '../types/resources.type';

export const getTTLFromAppConfig = async () => config.TTL;
export const getZRRFromAppConfig = async () => config.ZRR;
export const getAllPropertiesFromAppConfig = async () => config;
export const updateZRR = async (newZRR: Coordinates[]) => {
  config.ZRR = newZRR;
};
export const updateTTL = async (newTTL: number) => {
  config.TTL = newTTL;
};
