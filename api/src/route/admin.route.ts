import express from 'express';

import {
  generateOneGoldingue,
  handleTTLOperation,
  handleZRROperation,
} from '../controller/admin/AdminOperationController';
import resources from '../data/resources.fixtures';
import { checkTokenMiddleware } from '../middleware/check-token-middleware';
import { getAllPropertiesFromAppConfig } from '../services/admin-service';
import {
  AdminCreateOneGoldingueRequestType,
  AdminRequestPayload,
  AdminZRRUpdateRequestType,
} from '../types/admin.type';
import { isDefined } from '../utils/is-null-or-undefined';

const router = express.Router();

/**
 * If user is root return the config of the app.
 */
router.get('/', checkTokenMiddleware, async (req: AdminRequestPayload, res) => {
  if (!req.body.isAdmin) return res.status(403).send('You need to be root');
  const appConfig = await getAllPropertiesFromAppConfig();
  return res.status(200).send(appConfig);
});

/**
 * Create a new goldingue with position if user is root.
 */
router.post(
  '/goldingue',
  checkTokenMiddleware,
  async (req: AdminCreateOneGoldingueRequestType, res) => {
    if (!req.body.isAdmin) return res.status(403).send('You need to be root');
    if (
      !isDefined(req.body.position?.latitude) ||
      !isDefined(req.body.position?.longitude)
    )
      return res.status(400).send('Invalid position object');
    const goldingueCreated = await generateOneGoldingue(resources, req);
    res.status(200).send(goldingueCreated);
  },
);

/**
 * Check if params is valid and user root and calculate the four corner
 * of the rectangle to define ZRR area for the game.
 */
router.put(
  '/zrr',
  checkTokenMiddleware,
  async (req: AdminZRRUpdateRequestType, res) => {
    if (!req.body.isAdmin) return res.status(403).send('You need to be root');
    const { status, message } = await handleZRROperation(req);
    res.status(status).send(message);
  },
);

/**
 * Update the TTL of the Application if user is authenticated as root.
 */
router.put('/ttl', checkTokenMiddleware, async (req, res) => {
  if (!req.body.isAdmin) return res.status(403).send('You need to be root');
  const { status, message } = await handleTTLOperation(req);
  res.status(status).send(message);
});

export default router;
