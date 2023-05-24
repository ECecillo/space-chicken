echo "VITE_NODE_ENV=$NODE_ENV" > .env \
 && echo "VITE_CLIENT_AUTHENTICATION_API=$AUTH_PATH">> .env \
 && echo "VITE_CLIENT_RESOURCES_API=$API_PATH">> .env \
 && npm install --include=dev \
 &&  npm run build \
 &&  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY dist/* gitlabci@192.168.75.14:/var/www/html/ \
