import {
  getAllPropertiesFromAppConfig,
  updateTTL,
  updateZRR,
} from '../../services/admin-service';
import {
  AdminCreateOneGoldingueRequestType,
  AdminZRRUpdateRequestType,
} from '../../types/admin.type';
import { TypedRequestBody } from '../../types/express.type';
import {
  Coordinates,
  Resource,
  ResourceRole,
  ResourcesStore,
} from '../../types/resources.type';
import getRectangleGPSPoints from '../../utils/calculate-rectangle-from-two-coordinate';

const isValidCoord = (coordinateObject: Coordinates) =>
  coordinateObject && coordinateObject.latitude && coordinateObject.longitude;

/*
 * Generate a new goldingue with a given position, add it to the ResourcesStore and return it.
 */
export const generateOneGoldingue = async (
  resources: ResourcesStore,
  req: AdminCreateOneGoldingueRequestType,
): Promise<Resource> => {
  const date = Date.now();
  const { TTL } = await getAllPropertiesFromAppConfig();
  const newGoldingue = {
    id: `goldingue${Math.floor(Math.random() * 30000)}`, // 30 000 max.
    role: ResourceRole.GOLDINGUE,
    position: req.body.position,
    ttl: TTL,
    nests: 0,
    nuggets: 0,
    dateCreation: date,
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
  return { status: 204, message: '' };
};
