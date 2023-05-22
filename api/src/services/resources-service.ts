import { AxiosRequestConfig } from 'axios';

import { apiRequest } from '../config/axios.config';
import { Coordinates, ResourceRole, ResourcesStore } from '../types/resources.type';

export const findFirstResourceById = async (
  ressources: ResourcesStore,
  resourceId: string,
) => ressources.find((resource) => resource.id === resourceId);

const getUserRole = async (userLogin: string) => {
  // Send request with axios to /resources/users
  const config: Partial<AxiosRequestConfig> = {
    method: 'GET',
    url: `/users/${userLogin}`,
  };
  return apiRequest(config);
};

export const createNewPlayer = async (
  resources: ResourcesStore,
  userLogin: string,
  newCoordinates: Coordinates,
) => {
  const response = await getUserRole(userLogin);
  if (response.status === 404) throw new Error();
  const { role }: { role: string } = response.data;
  const newResource = {
    id: userLogin,
    position: newCoordinates,
    role: role === 'CHICKEN' ? ResourceRole.CHICKEN : ResourceRole.COWBOY,
    nests: 0,
    ttl: 0,
    nuggets: 0,
    dateCreation: Date.now(),
  };
  const newResourcesLength = resources.push(newResource);
  return resources.at(newResourcesLength - 1);
};

export const updatePositionOrCreateUser = async (
  resources: ResourcesStore,
  userLogin: string,
  newCoordinates: Coordinates,
): Promise<{ status: number; message: string }> => {
  const userStored = await findFirstResourceById(resources, userLogin);
  if (!userStored) {
    await createNewPlayer(resources, userLogin, newCoordinates);
    return { status: 201, message: 'New Resource created.' };
  }
  userStored!.position = newCoordinates;
  return { status: 204, message: 'New resource' };
};
