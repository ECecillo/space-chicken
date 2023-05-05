import express from 'express';
import request from 'supertest';

import router from '../../../src/route/resources.route';
import { signInUser } from '../../utils';

const server = express();
server.use(express.json());
server.use('/api/resources', router);
const app = server.listen(3832);

describe('PUT /resources/:userLogin/position', () => {
  let token: string;
  let userLogin: string;
  afterAll(async () => app.close());
  beforeAll(async () => {
    userLogin = 'John';
    token = await signInUser({ login: userLogin, password: 'johnPassword' });
  });
  it('should return 204 on successful update of user position', async () => {
    const position = { latitude: 45.781987907026334, longitude: 4.865596890449525 };

    const response = await request(app)
      .put(`/api/resources/${userLogin}/position`)
      .send({ position })
      .set('Authorization', `Bearer ${token}`);

    expect(response.status).toBe(204);
  });

  it('should return 400 when position object is invalid', async () => {
    const position = { latitude: null, longitude: 4.865596890449525 };

    const response = await request(app)
      .put(`/api/resources/${userLogin}/position`)
      .send({ position })
      .set('Authorization', `Bearer ${token}`);

    expect(response.status).toBe(400);
    expect(response.text).toBe('Invalid position object');
  });

  it('should return 401 when user is not authorized to update resource', async () => {
    const differentUserLoginParam = 'ECecillo';
    const position = { latitude: 45.781987907026334, longitude: 4.865596890449525 };

    const response = await request(app)
      .put(`/api/resources/${differentUserLoginParam}/position`)
      .send({ position })
      .set('Authorization', `Bearer ${token}`);

    expect(response.status).toBe(401);
    expect(response.text).toBe('Forbidden Operation');
  });
});
