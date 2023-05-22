import express from 'express';
import request from 'supertest';

import admin from '../../../src/route/admin.route';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json());
app.use('/admin', admin);
const server = app.listen(5382);

describe('GET /admin', () => {
  let adminToken: string;
  let regularUserToken: string;

  afterAll(async () => server.close());
  beforeAll(async () => {
    adminToken = await signInUser({ login: 'admin', password: 'root' });
    regularUserToken = await signInUser({ login: 'ECecillo', password: 'root' });
  });
  test('returns app config when user is root', async () => {
    const response = await request(server)
      .get('/admin')
      .set('Authorization', `Bearer ${adminToken}`);

    expect(response.status).toBe(200);
    expect(response.body).toEqual({
      TTL: 60,
      ZRR: [
        { latitude: 45.78237323836341, longitude: 4.8676273226737985 },
        { latitude: 45.781625019739884, longitude: 4.8676273226737985 },
        { latitude: 45.781625019739884, longitude: 4.863571822643281 },
        { latitude: 45.78237323836341, longitude: 4.863571822643281 },
      ],
    });
  });

  test('returns 403 Forbidden when user is not root', async () => {
    const response = await request(server)
      .get('/admin')
      .set('Authorization', `Bearer ${regularUserToken}`);

    expect(response.status).toBe(403);
    expect(response.text).toBe('You need to be root');
  });
});
