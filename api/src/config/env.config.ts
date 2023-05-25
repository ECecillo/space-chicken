import * as dotenv from 'dotenv';

dotenv.config();

const rootUrl =
  process.env.NODE_ENV === 'production'
    ? process.env.SPRING_SERVER
    : process.env.LOCAL;

const serverPort = process.env.EXPRESS_PORT || 3376;

export { rootUrl, serverPort };
