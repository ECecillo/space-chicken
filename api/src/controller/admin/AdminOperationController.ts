import {
  getAllPropertiesFromAppConfig,
  updateTTL,
  updateZRR,
} from '../../services/admin-service';
import { AdminZRRUpdateRequestType } from '../../types/admin.type';
import { TypedRequestBody } from '../../types/express.type';
import {
  Coordinates,
  Resource,
  ResourceRole,
  ResourcesStore,
} from '../../types/resources.type';
import getRectangleGPSPoints from '../../utils/calculate-rectangle-from-two-coordinate';
import generateRandomCoordinate from '../../utils/random-coordinates-given-area';

const isValidCoord = (coordinateObject: Coordinates) =>
  coordinateObject && coordinateObject.latitude && coordinateObject.longitude;

/*
 * Generate a new goldingue, add it to the ResourcesStore and return it.
 */
export const generateGoldingue = async (
  resources: ResourcesStore,
): Promise<Resource> => {
  const { TTL, ZRR } = await getAllPropertiesFromAppConfig();
  const newGoldingue = {
    id: `goldingue${Math.floor(Math.random() * 30000)}`, // 30 000 max.
    role: ResourceRole.GOLDINGUE,
    position: generateRandomCoordinate(ZRR[0], ZRR[1]),
    ttl: TTL,
    nests: 0,
    nuggets: 0,
  };
  resources.push(newGoldingue);
  return newGoldingue;
};

export const handleZRROperation = async (
  req: AdminZRRUpdateRequestType,
): Promise<{
  status: number;
  message: string | Array<Coordinates>;
}> => {
  const [highCornerLeftCoordinates, lowCornerRightCoordinates] =
    req.body.newPosition;

  if (
    !isValidCoord(highCornerLeftCoordinates) ||
    !isValidCoord(lowCornerRightCoordinates)
  ) {
    return { status: 400, message: 'Invalid position object' };
  }
  const newCoordinatesCalculated = getRectangleGPSPoints(
    highCornerLeftCoordinates,
    lowCornerRightCoordinates,
  );
  await updateZRR(newCoordinatesCalculated);
  return { status: 200, message: newCoordinatesCalculated };
};

export const handleTTLOperation = async (
  req: TypedRequestBody<{ ttl: number }>,
): Promise<{ status: number; message: string }> => {
  const { ttl: newTTL } = req.body;
  if (!newTTL || newTTL <= 0) return { status: 400, message: 'Invalid TTL' };
  await updateTTL(newTTL);
  return { status: 204, message: 'TTL Successfully updated.' };
};
