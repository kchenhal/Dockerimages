docker run -d --net mynet -p 80:80 -e CONSUL_HOST_NAME=172.31.54.85 -e CONSUL_HOST_PORT=8500 kchen007/state80
docker run -d -h state.ienergy.devcommunity -p 8080:8080 -e CONSUL_HOST_NAME=consul.ienergy.devcommunity -e CONSUL_HOST_PORT=8500 kchen007/state
