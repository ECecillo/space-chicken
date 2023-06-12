echo "API_PATH=$API_PATH" >.env &&
  echo "SPRING_URL=$SPRING_URL" >>.env &&
  npm install --include=dev &&
  npm run build &&
  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY ./public/* forgeci@51.38.178.218:/var/www/html/secret
