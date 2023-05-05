npm install --include=dev \
&&  npm run build \
&&  scp -r -o StrictHostKeyChecking=no -i $CI_SSH_KEY dist/* gitlabci@192.168.75.14:/var/www/html/ \
