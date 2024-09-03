# Environement Variables

```sh
# database file path, default: {workDir}/data
APP_DB_FILE_PATH="/your_path"

# access log directory path
APP_ACCESS_LOG_DIR="/your_dir"
```

# Docker Image

https://github.com/eatgrass/manager-demo/pkgs/container/manager-demo

```sh
docker pull ghcr.io/eatgrass/manager-demo:{version}
```


# Usage

## Request and Response
```sh
# create user
curl -X POST "http://localhost:8080/admin/addUser" \
-H "Authorization: eyJ1c2VySWQiOiAxLCAicm9sZSI6ICJhZG1pbiIsICJhY2NvdW50TmFtZSI6ICJEYXZpZCJ9" \
-H "Content-Type: application/json" \
-d '{"userId": 123, "endpoint": ["resource A", "resource B"]}' -i

# access user resource: A
curl -X GET "http://localhost:8080/user/A" \
-H "Authorization: eyJ1c2VySWQiOiAxMjMsICJyb2xlIjogInVzZXIiLCAiYWNjb3VudE5hbWUiOiAiTHVjeSJ9" -i\
```


## Log

default path: `/app/logs`

```sh
/app/logs # cat access_log.2024-09-03.log 
[2024-09-03 06:00:49.952] "-" 400 44909 From - "-"
[2024-09-03 06:00:50.155] "GET / HTTP/1.1" 404 201539 From - "-"
[2024-09-03 06:01:06.160] "-" 400 2265 From - "-"
[2024-09-03 06:01:06.173] "GET / HTTP/1.1" 404 9030 From - "-"
[2024-09-03 06:01:38.179] "-" 400 1515 From - "-"
[2024-09-03 06:01:38.188] "GET / HTTP/1.1" 404 6729 From - "-"
[2024-09-03 06:01:45.938] "GET / HTTP/1.1" 404 41824 From - "-"
[2024-09-03 06:01:46.191] "GET /favicon.ico HTTP/1.1" 404 14506 From - "-"
[2024-09-03 06:01:56.38] "POST / HTTP/1.1" 404 6338 From - "-"
[2024-09-03 06:02:42.193] "-" 400 1359 From - "-"
[2024-09-03 06:02:42.199] "GET / HTTP/1.1" 404 5279 From - "-"
[2024-09-03 06:09:56.850] "GET /user/A HTTP/1.1" 401 69892 From 123 "user [123] doesn't exist"
[2024-09-03 06:10:35.364] "POST /admin/addUser HTTP/1.1" 200 50684 From 1 "-"
[2024-09-03 06:10:49.66] "-i /admin/addUser HTTP/1.1" 405 41872 From 1 "-"
[2024-09-03 06:11:39.695] "POST /admin/addUser HTTP/1.1" 200 4809 From 1 "-"
[2024-09-03 06:12:20.512] "GET /user/A HTTP/1.1" 200 8215 From 123 "-"
```

## Database File

default path: `/app/data/database`

```sh
/app/data # cat database 
user|123|123|user|resource A|resource B
user|123|123|user|resource A|resource B
```
