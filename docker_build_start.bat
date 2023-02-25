docker-compose -f "docker-compose-debug.yml" --env-file ".\docker\.env" build
docker-compose -f "docker-compose-debug.yml" --env-file ".\docker\.env" up -d