import express from 'express';

import { checkTokenMiddleware } from '../middleware/check-token-middleware';

const router = express.Router();

/**
 * Testing purpose only to see if checkTokenMiddleware work properly.
 * Since the function is executed before we get here and we put the userLogin
 * in the body in the meantime, we should be able to access the userLogin.
 */
router.get('/', checkTokenMiddleware, async (req, res) => {
  const { userLogin } = req.body;
  return res.json({ login: userLogin });
});

export default router;
