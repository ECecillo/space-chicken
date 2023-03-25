/* Récupération du header bearer */
/**
 * Récupération du header bearer
 * @param headerValue
 * @returns
 */
export const extractBearerToken = (headerValue: string) => {
  if (!headerValue) return false;
  const tokenParsed = headerValue.match(/(bearer)\s+(\S+)/i);
  // Check if token parsed not null and return raw token.
  return tokenParsed && tokenParsed[2];
};
