#! /bin/bash
POSTGRES_ID=$(docker ps -aqf "name=postgres")
RESULT=$(docker exec $POSTGRES_ID psql -U myuser mydb -c "UPDATE users SET roles = '{0, 1, 2, 3}' WHERE email = '$1';")