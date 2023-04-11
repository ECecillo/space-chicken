import express from 'express';
import request from 'supertest';

import admin from '../../../src/route/admin.route';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json());
app.use('/admin', admin);
const server = app.listen(3001);

describe('PUT /admin/zrr', () => {
  let adminToken: string;
  let regularUserToken: string;

  afterAll(async () => server.close());
  beforeAll(async () => {
    adminToken = await signInUser({ login: 'admin', password: 'root' });
    regularUserToken = await signInUser({ login: 'ECecillo', password: 'root' });
  });
  it('returns 403 Forbidden when user is not admin', async () => {
    const response = await request(app)
      .put('/admin/zrr')
      .set('Authorization', `Bearer ${regularUserToken}`)
      .send({
        newPosition: [
          { latitude: 0, longitude: 0 },
          { latitude: 1, longitude: 1 },
        ],
      });

    expect(response.statusCode).toBe(403);
    expect(response.text).toBe('You need to be root');
  });

  it('returns 400 Bad Request when position object is invalid (1)', async () => {
    const response = await request(app)
      .put('/admin/zrr')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({
        newPosition: [{ latitude: 0 }, { longitude: 1 }],
      });

    expect(response.statusCode).toBe(400);
    expect(response.text).toBe('Invalid position object');
  });

  it('returns 400 Bad Request when position object is invalid (2)', async () => {
    const response = await request(app)
      .put('/admin/zrr')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({
        newPosition: [{ latitude: 0, longitude: 1 }, { longitude: 1 }],
      });

    expect(response.statusCode).toBe(400);
    expect(response.text).toBe('Invalid position object');
  });

  it('returns 200 OK and updates the ZRR coordinates when the request is valid', async () => {
    const newPosition = [
      { latitude: 45.512453, longitude: 4.912279 },
      { latitude: 45.510417, longitude: 4.914461 },
    ];
    const expectedResponse = {
      status: 200,
      message: [
        { latitude: 45.512453, longitude: 4.912279 },
        { latitude: 45.510417, longitude: 4.914461 },
        { latitude: 45.512453, longitude: 4.914461 },
        { latitude: 45.510417, longitude: 4.912279 },
      ],
    };

    const response = await request(app)
      .put('/admin/zrr')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ newPosition });

    expect(response.statusCode).toBe(expectedResponse.status);
    expect(response.body).toEqual(expectedResponse.message);
  });
});
