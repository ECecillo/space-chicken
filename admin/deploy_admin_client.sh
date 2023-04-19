echo "API_PATH=$API_PATH" >.env &&
  echo "SPRING_URL=$SPRING_URL" >>.env &&
  npm install --include=dev &&
  npm run build &&
  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY ./public/* gitlabci@192.168.75.14:/var/www/html/secret
