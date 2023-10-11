##docker commands
docker stop scaffold_gateway

docker rmi -f scaffold/gateway

docker build -t scaffold/gateway .

docker run -d --name scaffold_gateway -e "SPRING_PROFILES_ACTIVE=docker" -p 8080:8080 -t scaffold/gateway

