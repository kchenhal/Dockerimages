docker -H tcp://localhost:2375 run -p 80:80  -d  ehazlett/interlock --swarm-url tcp://172.31.40.56:3375 --plugin haproxy start
