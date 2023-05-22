import { Resource, ResourceRole, ResourcesStore } from '../src/types/resources.type';

/**
 * Factory used to create a new resource for test purpose.
 * @param resources
 * @returns create a new resource in static store, if role not provided will be a goldingue.
 */
export function resourceFactory(resources: ResourcesStore) {
  async function prepapreResource(inputResource: Partial<Resource>) {
    const role = inputResource.role ?? ResourceRole.GOLDINGUE;
    const id = inputResource.id ?? `Resource_ID_${role}`;
    const nests = inputResource.nests ?? 0;
    const nuggets = inputResource.nuggets ?? 0;
    const ttl = inputResource.ttl ?? 60;
    const position = inputResource.position ?? { longitude: 45.2163, latitude: 4.5 };
    return {
      dateCreation: Date.now(),
      id,
      role,
      nests,
      nuggets,
      ttl,
      position,
    };
  }
  return async function createResource(inputResource: Partial<Resource>) {
    const prepapredResource = await prepapreResource(inputResource);
    resources.push(prepapredResource);
    return prepapredResource;
  };
}
