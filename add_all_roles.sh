#! /bin/bash
POSTGRES_ID=$(docker ps -aqf "name=postgres")
echo $POSTGRES_ID
docker exec $POSTGRES_ID psql -U myuser mydb -c "UPDATE users SET roles = '{0, 1, 2, 3}' WHERE email = '$1';"