import * as dotenv from 'dotenv';

dotenv.config();

const rootUrl =
  process.env.NODE_ENV === 'production' ? process.env.PROD : process.env.LOCAL;

const serverPort = process.env.PORT || 3000;

export { rootUrl, serverPort };
