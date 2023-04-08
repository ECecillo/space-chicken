import * as dotenv from 'dotenv';
import express from 'express';
import path from 'path';

import { serverPort } from './config/env.config';
import { sendResponse } from './handlers/error/handle-error-response';
import admin from './route/admin.route';
import resources from './route/resources.route';

dotenv.config();

const cors = require('cors');

const port = serverPort;
const dirname = path.resolve();
const options = {
  root: path.join(dirname, 'public'),
};
export const app = express();

app.use(express.json()); // Permet de parser le body des requêtes.
app.use(express.urlencoded({ extended: true }));
app.use(cors({ origin: '*' })); // Permet de gérer les requêtes cross-origin (CORS).
// Traite les requêtes issues du dossier public (http://localhost:3000/static/...)d'un serveur front.

app.use('/static', express.static('public'));
app.use('/api/resources', resources);
app.use('/admin', admin);

app.get('/', (_req, res) => {
  res.status(200).sendFile('/index.html', options);
});

// Add 404 handler.
app.use((req, res) => {
  sendResponse(req, res, 404, true, undefined, options);
});

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});
