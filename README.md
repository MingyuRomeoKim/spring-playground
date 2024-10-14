# spring-playground 동작 전 사전 준비

## 1. mysql 설치 및 실행
### 1.1. docker desktop install (mac)
```shell
 sudo hdiutil attach Docker.dmg
 sudo /Volumes/Docker/Docker.app/Contents/MacOS/install
 sudo hdiutil detach /Volumes/Docker
```
### 1.2. mysql image install & run
```shell
docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:latest
```
### 1.3 mysql 접속 및 playground database 생성
```shell
docker exec -it mysql bash
mysql -u root -p
create database playground;
create database playground_test;
```

