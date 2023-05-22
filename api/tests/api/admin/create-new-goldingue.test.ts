import express from 'express';
import request from 'supertest';

import admin from '../../../src/route/admin.route';
import { Resource, ResourceRole } from '../../../src/types/resources.type';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json());
app.use('/admin', admin);
const server = app.listen(3436);

describe('POST /admin/goldingue', () => {
  let adminToken: string;
  let regularUserToken: string;

  afterAll(async () => server.close());
  beforeAll(async () => {
    adminToken = await signInUser({ login: 'admin', password: 'root' });
    regularUserToken = await signInUser({ login: 'ECecillo', password: 'root' });
  });

  it('should return a 403 status if the user is not root', async () => {
    const res = await request(server)
      .post('/admin/goldingue')
      .set('Authorization', `Bearer ${regularUserToken}`)
      .send();

    expect(res.status).toEqual(403);
    expect(res.text).toEqual('You need to be root');
  });

  it('should return a 400 status if no position object has been passed in query', async () => {
    const res = await request(server)
      .post('/admin/goldingue')
      .set('Authorization', `Bearer ${adminToken}`)
      .send();

    expect(res.status).toEqual(400);
    expect(res.text).toEqual('Invalid position object');
  });

  it('should create a new goldingue and return it with a 200 status if the user is root and the position have been given', async () => {
    const res = await request(server)
      .post('/admin/goldingue')
      .set('Authorization', `Bearer ${adminToken}`)
      .send({ position: { latitude: 0, longitude: 0 } });

    expect(res.status).toEqual(200);
    const { body }: { body: Resource } = res;
    expect(body).toHaveProperty('id');
    expect(body).toHaveProperty('role', ResourceRole.GOLDINGUE);
    expect(body).toHaveProperty('position', { latitude: 0, longitude: 0 });
    expect(body).toHaveProperty('ttl', 60); // default value in the app.
  });
});
