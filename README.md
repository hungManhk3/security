# Security

## Yêu cầu
- Docker >= 20.x, Docker Compose V2 (hoặc `docker compose` CLI)
- (Dev) Java, Maven

## Chuẩn bị
1. Sao chép file mẫu `.env.example` thành `.env` và chỉnh thông tin.
2. Kiểm tra `init.sql` nếu muốn seed DB mặc định.

## Chạy
## build image
- tai noi chua cung cap Dockerfile:
-  docker build -t identity-service:0.0.1 .    

## tao container cho app
docker run --name identity-service --network security_spring-demo -p 8080:8080 -e DBMS_CONNECTION=jdbc:postgresql://postgres-sql-s:5432/security --env-file .env identity-service:0.0.1

docker compose -p security -f compose/docker-compose.yml up -d (prod-like)

docker compose -p security-dev -f compose/docker-compose.dev.yml up -d

docker compose -f docker-compose..dev.yml down

File `.env` (nằm cùng thư mục với `docker-compose.yml`):

```env
# Cổng app sẽ bind ra ngoài (host)
APP_PORT=8085

# Postgres
POSTGRES_USER=username
POSTGRES_PASSWORD=password
POSTGRES_DB=security
