import express from 'express';
import request from 'supertest';

import resources from '../../../src/data/resources.fixtures';
import admin from '../../../src/route/admin.route';
import { Resource, ResourceRole } from '../../../src/types/resources.type';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json());
app.use('/admin', admin);
const server = app.listen(3001);

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

  it('should create a new goldingue and return it with a 200 status if the user is root', async () => {
    const res = await request(server)
      .post('/admin/goldingue')
      .set('Authorization', `Bearer ${adminToken}`)
      .send();

    expect(res.status).toEqual(200);
    const { body }: { body: Resource } = res;
    expect(body).toHaveProperty('id');
    expect(body).toHaveProperty('role', ResourceRole.GOLDINGUE);
    expect(body).toHaveProperty('position');
    expect(body).toHaveProperty('ttl', 60); // default value in the app.
    expect(resources).toHaveLength(9);
  });
});
