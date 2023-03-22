import express from 'express';
import path from 'path';

import { sendResponse } from './handlers/error/handle-error-response';

import { serverPort } from './config/env.config';
import auth from './route/auth';

const app = express();
const port = serverPort;

const dirname = path.resolve();
const options = {
  root: path.join(dirname, 'public'),
};

app.use(express.json()); // Permet de parser le body des requêtes.
app.use(express.urlencoded({ extended: true }));
app.use('/api/auth', auth);
app.use('/static', express.static('public'));
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
