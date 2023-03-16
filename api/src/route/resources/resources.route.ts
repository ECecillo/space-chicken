import express from 'express';

// eslint-disable-next-line max-len
import { handleResourceOperation } from '../../controller/resources/operations/ResourcesOperationController';
import resources from '../../data/resources.fixtures';
import {
  filterAllValidResources,
  updateUserPosition,
} from '../../services/resources-service';

const router = express.Router();

/**
 * Returns an array containing the representations of all existing
 *  resources in the game.
 */
router.get('/', async (_, res) => {
  const resourcesFound = await filterAllValidResources(resources);
  res.status(200).send(resourcesFound);
});

/**
 * WIP ajouter le midlleware qui ajoute le login de l'utilisateur au body.
 * /resources/{resourceId}
 * Cow-boy user preleves gold or chicken user builds nest
 */
router.post('/:id', async (req, res) => {
  const result = await handleResourceOperation(req, resources);
  return res.status(result.status).send(result.message);
});

/**
 * /resources/{resourceId}/position
 * Send a LatLng object to the server to update user location in map.
 */
router.put('/:userLogin/position', (req, res) => {
  const { position: newUserPosition } = req.body;
  if (!newUserPosition) return res.status(400).send('Invalid position object');

  const { userLogin } = req.params;
  try {
    updateUserPosition(resources, userLogin, newUserPosition);
    return res.status(204).send(resources);
  } catch {
    return res.status(400).send('User not found');
  }
});

export default router;
