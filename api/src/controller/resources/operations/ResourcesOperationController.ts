import { OperationType } from '../../../model/enum';
import { findFirstResourceById } from '../../../services/resources-service';
import { TypedRequestBody } from '../../../types/express.type';
import { Resource, ResourcesStore } from '../../../types/resources.type';
import distance from '../../../utils/distance-between-two-coordinates';

type IncrementPlayerResource = { operationType: string; userLogin: string };
type VerifyOperationRetunedPayload = { status: number; message: string };

const verifyGrabNuggetOperationIsValidForPlayer = (
  player: Resource,
  distanceBetweenPlayerAndGoldingue: number,
): VerifyOperationRetunedPayload => {
  // decrease ttl of goldingue.
  if (player.role !== 'cow-boy')
    return { status: 400, message: 'Invalid operation type for this user.' };
  if (distanceBetweenPlayerAndGoldingue >= 5)
    return { status: 403, message: 'User too far from resource to modify it.' };
  return { status: 202, message: '' };
};

const verifyBuildNestOperationIsValidForPlayer = (
  player: Resource,
  distanceBetweenPlayerAndGoldingue: number,
): VerifyOperationRetunedPayload => {
  if (player.role !== 'chicken')
    return { status: 400, message: 'Invalid operation type for this user.' };
  if (distanceBetweenPlayerAndGoldingue >= 5)
    return { status: 403, message: 'User too far from resource to modify it.' };
  return { status: 202, message: '' };
};

export async function handleResourceOperation(
  req: TypedRequestBody<IncrementPlayerResource>,
  resources: ResourcesStore,
): Promise<{ status: number; message: string }> {
  const { operationType, userLogin } = req.body;
  const goldinguePlantId = req.params.id;

  const goldingueFound = await findFirstResourceById(resources, goldinguePlantId);
  const player = await findFirstResourceById(resources, userLogin);
  if (!player || !goldingueFound)
    return { status: 404, message: 'Resource not found' };
  const distanceBetweenPlayerAndGoldingue = distance(
    player.position,
    goldingueFound.position,
  );
  switch (operationType) {
    case OperationType['grab gold nugget']:
      const validOperationForCOWBOY = verifyGrabNuggetOperationIsValidForPlayer(
        player,
        distanceBetweenPlayerAndGoldingue,
      );
      if (validOperationForCOWBOY.status !== 202) return validOperationForCOWBOY;
      player.nuggets += 1;
      goldingueFound.ttl -= 1;
      break;
    case OperationType['build nest']:
      const validOperationForCHICKEN = verifyBuildNestOperationIsValidForPlayer(
        player,
        distanceBetweenPlayerAndGoldingue,
      );
      if (validOperationForCHICKEN.status !== 202) return validOperationForCHICKEN;
      player.nests += 1;
      goldingueFound.ttl -= 1;
      break;
    default:
      return {
        status: 400,
        message: 'Invalid operation type or not a goldingue plant',
      };
  }

  return { status: 204, message: 'successful operation' };
}
