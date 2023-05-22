import express from 'express';
import request from 'supertest';

import resources from '../../../src/data/resources.fixtures';
import router from '../../../src/route/resources.route';
import { ResourceRole, ResourcesStore } from '../../../src/types/resources.type';
import { resourceFactory } from '../../test-helpers';
import { signInUser } from '../../utils';

const app = express();
app.use(express.json());
app.use('/api/resources', router);
const server = app.listen(3343);

const createResource = resourceFactory(resources);

describe('POST /api/resources/:id', () => {
  let jwtTokenChicken: string;
  let jwtTokenCOWBOY: string;
  const cowboy = 'Elfenwaar' as const;
  const chicken = 'ECecillo' as const;
  afterAll(async () => server.close());
  beforeAll(async () => {
    jwtTokenChicken = await signInUser({ login: chicken, password: 'root' });
    jwtTokenCOWBOY = await signInUser({ login: cowboy, password: 'root' });
  });
  test('returns a 204 status code and successful operation message when valid input is provided (CHICKEN)', async () => {
    const goldingueToUpdate = 'goldingue0';
    const response = await request(app)
      .post(`/api/resources/${goldingueToUpdate}`)
      .send({
        operationType: 'build nest',
      })
      .set('Authorization', `Bearer ${jwtTokenChicken}`)
      .set('Accept', 'application/json')
      .expect(204);
    expect(response.body).toEqual({});

    const { body: allResources }: { body: ResourcesStore } = await request(app)
      .get('/api/resources')
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenChicken}`);

    expect(
      allResources.find((resource) => resource.id === chicken)?.nests,
    ).toBeGreaterThan(0);
    // Since the data is static and already in memory, the default ttl is 60.
    expect(
      allResources.find((resource) => resource.id === goldingueToUpdate)?.ttl,
    ).toBeLessThan(60);
  });
  test('returns a 204 status code and successful operation message when valid input is provided (COWBOY)', async () => {
    const goldingueToUpdate = 'goldingue1';
    const response = await request(app)
      .post(`/api/resources/${goldingueToUpdate}`)
      .send({
        operationType: 'grab gold nugget',
      })
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .set('Accept', 'application/json')
      .expect(204);
    expect(response.body).toEqual({});

    const { body: allResources }: { body: ResourcesStore } = await request(app)
      .get('/api/resources')
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`);

    expect(
      allResources.find((resource) => resource.id === cowboy)?.nuggets,
    ).toBeGreaterThan(0);
    // Since the data is static and already in memory, the default ttl is 60.
    expect(
      allResources.find((resource) => resource.id === goldingueToUpdate)?.ttl,
    ).toBeLessThan(60);
  });

  test('returns a 400 status code and error message when an invalid operation type is provided', async () => {
    const goldingueToUpdate = 'goldingue2';
    const response = await request(app)
      .post(`/api/resources/${goldingueToUpdate}`)
      .send({
        operationType: 'invalid operation',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .expect(400);

    expect(response.text).toEqual('Invalid operation type or not a goldingue plant');
  });

  test('returns a 400 status code and error message when operation type doesnt match user role (CHICKEN)', async () => {
    const goldingueId = 'GOLDINGUE_ID';
    await createResource({
      id: goldingueId,
      role: ResourceRole.GOLDINGUE,
      ttl: 60,
      position: { latitude: 45.78205707337908, longitude: 4.864875376224519 },
    });
    const response = await request(app)
      .post(`/api/resources/${goldingueId}`)
      .send({
        operationType: 'grab gold nugget',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenChicken}`)
      .expect(400);

    expect(response.text).toEqual('Invalid operation type for this user.');
  });

  test('returns a 400 status code and error message when operation type doesnt match user role (COWBOY)', async () => {
    const goldingueToUpdate = 'goldingue4';
    const response = await request(app)
      .post(`/api/resources/${goldingueToUpdate}`)
      .send({
        operationType: 'build nest',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .expect(400);

    expect(response.text).toEqual('Invalid operation type for this user.');
  });

  test('returns a 401 status code and error message when user token not valid', async () => {
    const goldingueToUpdate = 'goldingue5';
    const response = await request(app)
      .post(`/api/resources/${goldingueToUpdate}`)
      .send({
        operationType: 'build nest',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer invalid-token`)
      .expect(401);

    expect(response.text).toEqual('User authentication failed or expired');
  });

  test('returns a 404 status code and error message when the resource is not found', async () => {
    const response = await request(app)
      .post('/api/resources/456')
      .send({
        operationType: 'grab gold nugget',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .expect(404);

    expect(response.text).toEqual('Resource not found');
  });

  test('returns a 403 status code and error message when the resource can not be used anymore', async () => {
    const goldingueWithNullTTL = 'GoldingueWithNullTTL_ID';
    await createResource({
      id: goldingueWithNullTTL,
      role: ResourceRole.GOLDINGUE,
      ttl: 0,
    });
    const response = await request(app)
      .post(`/api/resources/${goldingueWithNullTTL}`)
      .send({ operationType: 'grab gold nugget' })
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .expect(403);

    expect(response.text).toEqual('The goldingue plant can not be use anymore.');
  });

  test('returns a 403 status code and error message when the user is too far from the resource', async () => {
    const goldingueTooFar = 'GoldingueTooFar_ID';
    await createResource({
      id: goldingueTooFar,
      role: ResourceRole.GOLDINGUE,
      position: { latitude: 0, longitude: 0 },
    });
    const response = await request(app)
      .post(`/api/resources/${goldingueTooFar}`)
      .send({
        operationType: 'grab gold nugget',
      })
      .set('Accept', 'application/json')
      .set('Authorization', `Bearer ${jwtTokenCOWBOY}`)
      .expect(403);

    expect(response.text).toEqual('User too far from resource to modify it.');
  });
});
