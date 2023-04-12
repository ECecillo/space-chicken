import { authentificationURL } from '../env'

export const login = async (login, password) => {
  console.log('Auth url: ', authentificationURL)
  const url = `${authentificationURL}/login`
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ login, password })
  }
  const response = await fetch(url, options)
  if (response.status !== 204) throw new Error('Login failed')
  return response
}
