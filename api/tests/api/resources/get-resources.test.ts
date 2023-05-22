import express from 'express';
import request from 'supertest';

import resources from '../../../src/data/resources.fixtures';
import resourcesRouter from '../../../src/route/resources.route';
import { signInUser } from '../../utils';

// Create a new instance of the server for test purpose.
const app = express();
app.use(express.json())
app.use('/api/resources', resourcesRouter);
const server = app.listen(3537);

describe('GET /api/resources', () => {
  let jwtToken: string;
  afterAll(async () => server.close());
  beforeAll(async () => {
    jwtToken = await signInUser({ login: 'ECecillo', password: 'root' });
  });
  it('should return 401 if no token is provided', async () => {
    const response = await request(app).get('/api/resources');
    expect(response.status).toBe(401);
    expect(response.text).toBe('Missing token');
  });

  it('should return 401 if an invalid token is provided', async () => {
    const response = await request(app)
      .get('/api/resources')
      .set('Authorization', 'Bearer invalid-token');
    expect(response.status).toBe(401);
    expect(response.text).toBe('User authentication failed or expired');
  });

  it('should return the resources if a valid token is provided', async () => {
    const response = await request(app)
      .get('/api/resources')
      .set('Authorization', `Bearer ${jwtToken}`);
    expect(response.status).toBe(200);
    expect(response.body).toEqual(resources); // assume 'resources' is defined somewhere
  });
});
