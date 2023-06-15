echo "EXPRESS_PORT=$EXPRESS_PORT" > .env \
  &&  echo "NODE_ENV=$NODE_ENV" >> .env \
  &&  echo "SPRING_SERVER=$SPRING_SERVER" >> .env \
  &&  cat .env \
  &&  npm install --include=dev \
  &&  npm run build \
  &&  cp .env build/.env \
  &&  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY public build .env package.json package-lock.json forgeci@51.38.178.218:/home/forgeci \
  &&  ssh -i $CI_SSH_KEY forgeci@51.38.178.218 "npm install --omit=dev" \
  &&  ssh -i $CI_SSH_KEY forgeci@51.38.178.218 "pm2 start build/server.js --name server || pm2 restart server"
