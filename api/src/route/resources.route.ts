import express from 'express';

import { handleResourceOperation } from '../controller/resources/operations/ResourcesOperationController';
import resources from '../data/resources.fixtures';
import { checkTokenMiddleware } from '../middleware/check-token-middleware';
import { updatePositionOrCreateUser } from '../services/resources-service';
import { MiddlewarePayload, TypedRequestBody } from '../types/express.type';
import { Coordinates } from '../types/resources.type';

const router = express.Router();

/**
 * Returns an array containing the representations of all existing
 *  resources in the game.
 */
router.get('/', checkTokenMiddleware, async (_, res) =>
  res.status(200).send(resources),
);

/**
 * /resources/{resourceId}
 * Cow-boy user preleves gold or chicken user builds nest
 */
router.post('/:id', checkTokenMiddleware, async (req, res) => {
  const result = await handleResourceOperation(req, resources);
  return res.status(result.status).send(result.message);
});

/**
 * /resources/{resourceId}/position
 * Send a LatLng object to the server to update user location in map
 * or fetch and create new resource in Resource Store.
 */
router.put(
  '/:userLogin/position',
  checkTokenMiddleware,
  async (
    req: TypedRequestBody<{ position: Coordinates } & MiddlewarePayload>,
    res,
  ) => {
    const { position: newUserPosition, userLogin: userSpring, isAdmin } = req.body;
    if (!newUserPosition || !newUserPosition.latitude || !newUserPosition.longitude)
      return res.status(400).send('Invalid position object');

    const { userLogin } = req.params;
    if (!isAdmin && userLogin !== userSpring)
      return res.status(401).send('Forbidden Operation');
    try {
      const result = await updatePositionOrCreateUser(
        resources,
        userLogin,
        newUserPosition,
      );
      return res.status(result.status).send(result.message);
    } catch {
      return res.status(400).send('User not found');
    }
  },
);

export default router;
