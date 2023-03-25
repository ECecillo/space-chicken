import { MiddlewarePayload, TypedRequestBody } from './express.type';
import { Coordinates } from './resources.type';

export type AdminRequestPayload = TypedRequestBody<MiddlewarePayload>;
export type AdminZRRUpdateRequestType = TypedRequestBody<
  MiddlewarePayload & { newPosition: [Coordinates, Coordinates] }
>;
