const environment = import.meta.env.VITE_NODE_ENV || 'development'

//TODO: Refactor for less code duplication
const authentificationURL =
  environment !== 'development'
    ? import.meta.env.VITE_CLIENT_AUTHENTICATION_API
    : import.meta.env.VITE_DEV_AUTHENTICATION_API
const resourceURL =
  environment !== 'development'
    ? import.meta.env.VITE_CLIENT_RESOURCES_API
    : import.meta.env.VITE_DEV_RESOURCES_API

export { authentificationURL, resourceURL }
