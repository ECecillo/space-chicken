import Express from 'express';

// Define a custom error handler to send an html page or a message.
export function sendResponse(
  req: Express.Request,
  res: Express.Response,
  statusCode: number,
  html?: true | false,
  message?: string,
  options?: Partial<{ root: string }>,
) {
  if (statusCode && options && html) {
    return res.status(statusCode).sendFile(`/error/${statusCode}.html`, options);
  }
  return res.status(statusCode).send(message);
}
