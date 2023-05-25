echo "EXPRESS_PORT=$EXPRESS_PORT" > .env \
  &&  echo "NODE_ENV=$NODE_ENV" >> .env \
  &&  echo "SPRING_SERVER=$SPRING_SERVER" >> .env \
  &&  cat .env \
  &&  npm install --include=dev \
  &&  npm run build \
  &&  npm prune --production \
  &&  npm ci \
  &&  mv .env public build \
  &&  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY build node_modules gitlabci@192.168.75.14:/home/gitlabci \
  &&  ssh -i $CI_SSH_KEY gitlabci@192.168.75.14 "pm2 restart server"
