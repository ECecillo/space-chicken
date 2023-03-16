import { Coordinates, ResourcesStore } from '../types/resources.type';

const filterAllValidResources = async (resources: ResourcesStore) =>
  resources.filter(
    (resource) =>
      ((resource.role === 'cow-boy' || resource.role === 'chicken') &&
        resource.position !== null &&
        resource.position !== undefined &&
        resource.position.longitude !== null &&
        resource.position.latitude !== null) ||
      (resource.role === 'goldingue' && resource.ttl > 0) ||
      (resource.role === 'chicken' && resource.nests > 0),
  );

const findFirstResourceById = async (
  resources: ResourcesStore,
  resourceId: string,
) => resources.find((resource) => resource.id === resourceId);

const updateUserPosition = async (
  resources: ResourcesStore,
  userLogin: string,
  newCoordinates: Coordinates,
) => {
  const userStored = await findFirstResourceById(resources, userLogin);
  if (!userStored) throw new Error();
  userStored.position = newCoordinates;
};

export { filterAllValidResources, findFirstResourceById, updateUserPosition };
