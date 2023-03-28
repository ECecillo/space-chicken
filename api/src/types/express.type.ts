import Express from 'express';
import { Query, Send } from 'express-serve-static-core';

export interface TypedRequestBody<T> extends Express.Request {
  body: T;
}

export interface TypedRequestQuery<T extends Query> extends Express.Request {
  query: T;
}

// If we want to type both body and query params.
export interface TypedRequest<T extends Query, U> extends Express.Request {
  body: U;
  query: T;
}

export interface TypedResponse<ResBody> extends Express.Response {
  json: Send<ResBody, this>;
}

export type MiddlewarePayload = {
  userLogin: string;
  isAdmin: boolean;
};
