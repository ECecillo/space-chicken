import express from 'express';

import { serverPort } from './config/env.config';
import auth from './route/auth';

const app = express();
const port = serverPort;

app.use(express.json()); // Permet de parser le body des requêtes.
app.use(express.urlencoded({ extended: true }));
app.use('/api/auth', auth);

app.get('/', (_, res) => {
  res.send('Express + TypeScript Server');
});

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});
