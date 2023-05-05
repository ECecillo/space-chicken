import { OperationType } from '../../../model/enum';
import { findFirstResourceById } from '../../../services/resources-service';
import { MiddlewarePayload, TypedRequestBody } from '../../../types/express.type';
import {
  Resource,
  ResourceRole,
  ResourcesStore,
} from '../../../types/resources.type';
import distance from '../../../utils/distance-between-two-coordinates';

type IncrementPlayerResource = MiddlewarePayload & { operationType: string };
type VerifyOperationRetunedPayload = { status: number; message: string };

const verifyGrabNuggetOperationIsValidForPlayer = (
  player: Resource,
): VerifyOperationRetunedPayload => {
  if (player.role !== 'cow-boy') {
    return { status: 400, message: 'Invalid operation type for this user.' };
  }
  return { status: 202, message: 'Player authorized to build.' };
};

const verifyBuildNestOperationIsValidForPlayer = (
  player: Resource,
): VerifyOperationRetunedPayload => {
  if (player.role !== 'chicken') {
    return { status: 400, message: 'Invalid operation type for this user.' };
  }
  return { status: 202, message: 'Player authorized to build.' };
};

export async function handleResourceOperation(
  req: TypedRequestBody<IncrementPlayerResource>,
  resources: ResourcesStore,
): Promise<{ status: number; message: string }> {
  const { operationType, userLogin } = req.body;
  const goldinguePlantId = req.params.id;

  const goldingueFound = await findFirstResourceById(resources, goldinguePlantId);
  if (!goldingueFound) return { status: 404, message: 'Resource not found' };
  if (goldingueFound.ttl <= 0)
    return { status: 403, message: 'The goldingue plant can not be use anymore.' };

  const player = await findFirstResourceById(resources, userLogin);
  if (!player) {
    return { status: 404, message: 'User not found in the app' };
  }

  const distanceBetweenPlayerAndGoldingue = distance(
    player.position,
    goldingueFound.position,
  );
  if (distanceBetweenPlayerAndGoldingue >= 5) {
    return { status: 403, message: 'User too far from resource to modify it.' };
  }

  switch (operationType) {
    case OperationType['grab gold nugget']:
      const validOperationForCOWBOY =
        verifyGrabNuggetOperationIsValidForPlayer(player);
      if (validOperationForCOWBOY.status !== 202) return validOperationForCOWBOY;
      player.nuggets += 1;
      goldingueFound.ttl = 0;
      goldingueFound.role = ResourceRole.NUGGETS;
      break;
    case OperationType['build nest']:
      const validOperationForCHICKEN =
        verifyBuildNestOperationIsValidForPlayer(player);
      if (validOperationForCHICKEN.status !== 202) return validOperationForCHICKEN;
      player.nests += 1;
      goldingueFound.ttl = 0;
      goldingueFound.role = ResourceRole.NEST;
      break;
    default:
      return {
        status: 400,
        message: 'Invalid operation type or not a goldingue plant',
      };
  }
  return { status: 204, message: 'Resource captured !' };
}
