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


***


## 2. redis 설치 및 실행
### 2.1 redis image install & run (local)
```shell
docker run -d --name redis -p 6379:6379 redis:latest redis-server --requirepass ""
```

### 2.2 redis cluster (prod)
### 2.2.1 redis-cluster 폴더 접속 및 docker 컨테이너 실행
```shell
 cd ./redis-cluster
 docker-compose up -d
```
### 2.2.2 redis cluster 생성
```shell
docker run -it --rm --network redis-cluster_redis-cluster redis:latest redis-cli --cluster create \
172.28.0.11:7001 \
172.28.0.12:7002 \
172.28.0.13:7003 \
172.28.0.14:7004 \
172.28.0.15:7005 \
172.28.0.16:7006 \
--cluster-replicas 1
```
### 2.2.3 cluster 구성 확인
2.2.2 명령어 실행 후 출력되는 내용을 확인하고 모든 노드가 올바르게 할당되었다면 yes 입력하여 클러스터 생성 완료하기.
```shell
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 172.28.0.4:7004 to 172.28.0.1:7001
Adding replica 172.28.0.5:7005 to 172.28.0.2:7002
Adding replica 172.28.0.6:7006 to 172.28.0.3:7003
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
...

Do you want to proceed with the proposed cluster configuration? (type 'yes' to accept): yes
```
### 2.2.4 cluster 상태 확인하기
```shell
docker run -it --rm --network redis-cluster_redis-cluster redis:latest redis-cli -c -h 172.28.0.1 -p 7001 cluster info
```
### 2.2.5 cluster 테스트
redis 클러스터에 키를 설정하고 가져와서 정상 작동 여부 테스트하기.
```shell
docker run -it --rm --network redis-cluster_redis-cluster redis:latest redis-cli -c -h 172.28.0.1 -p 7001
```
Redis cli에 접속되면 아래 명령어로 값 설정 및 가져오기 테스트
```redis
SET foo bar
GET foo
```


***

## 3. swagger - api 문서 확인
현재 디폴트 포트 8080 사용중입니다. api 문서는 security 설정이 되어 있지 않아 누구나 접근 가능합니다.

```html
http://localhost:8080/swagger-ui/index.html#/
``` 
