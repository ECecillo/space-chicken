import express from 'express';
import request from 'supertest';

import admin from '../../../src/route/admin.route';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json());
app.use('/admin', admin);
const server = app.listen(3001);

describe('PUT /admin/ttl', () => {
  let adminToken: string;
  let regularUserToken: string;

  afterAll(async () => server.close());
  beforeAll(async () => {
    adminToken = await signInUser({ login: 'admin', password: 'root' });
    regularUserToken = await signInUser({ login: 'ECecillo', password: 'root' });
  });

  it('should return 403 if user is not root', async () => {
    const response = await request(app)
      .put('/admin/ttl')
      .set('Authorization', `Bearer ${regularUserToken}`)
      .send({ ttl: 30 });

    expect(response.status).toBe(403);
    expect(response.text).toBe('You need to be root');
  });

  it('should return 400 if no valid ttl is provided', async () => {
    const response = await request(app)
      .put('/admin/ttl')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ ttl: -10 });

    expect(response.status).toBe(400);
    expect(response.text).toBe('Invalid TTL');
  });

  it('should update the TTL and return 204 on success', async () => {
    const response = await request(app)
      .put('/admin/ttl')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ ttl: 30 });

    const actualTTL = await request(app)
      .get('/admin')
      .set('Authorization', `Bearer ${adminToken}`);

    expect(response.status).toBe(204);
    expect(actualTTL.body.TTL).toBe(30);
  });
});
