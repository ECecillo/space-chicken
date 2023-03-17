/* eslint-disable class-methods-use-this */
/* eslint-env es6 */
import { AxiosRequestConfig } from 'axios';

import { apiRequest } from '../../config/axios.config';

export class UserController {
  /**
   * Check if token is still valid and return the user asccoiated to it.
   * @param token JWT without Bearer prefix.
   * @returns response of the request made to Spring server.
   */
  static async authenticate(token: string) {
    const config: Partial<AxiosRequestConfig> = {
      method: 'GET',
      url: '/authenticate',
      params: {
        jwt: token,
        Origin: 'http://localhost:8080',
      },
    };

    return apiRequest(config);
  }
}
