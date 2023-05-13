import { AxiosError } from 'axios';
import { NextFunction, Request, Response } from 'express';

import { UserController } from '../controller/user/user.controller';
import { extractBearerToken } from '../utils/extract-bearer-token';

/**
 * Middleware that will check for needed route if token has been passed in header
 * and validate it against Sping Server.
 * @param req Express.Request
 * @param res Express.Response
 * @param next Express.Next to chain call to the requested route.
 * @returns pass to the next route with the userLogin in the request body.
 */
export const checkTokenMiddleware = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  const token =
    req.headers.authorization && extractBearerToken(req.headers.authorization);

  // Présence d'un token
  if (!token) return res.status(401).send('Missing token');

  // Véracité du token
  // Remplacer par la requête vers /authenticate
  try {
    const response = await UserController.authenticate(token);
    const userLogin = response.headers['x-space-chicken-login'];
    if (response.status !== 204)
      return res.status(501).send('Not implemented handler');
    req.body.userLogin = userLogin;
    req.body.isAdmin = userLogin === 'admin';
    return next(); // Chain request.
  } catch (err) {
    const { response } = err as AxiosError;
    if (response?.status === 400) return res.status(400).send('Missing parameters');
    if (response?.status === 401)
      return res.status(401).send('User authentication failed or expired');
    return res.status(501).send(err.message);
  }
};
