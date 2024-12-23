# spring-playground 실행

## 0. README-INSTALL.md 참고
본 프로그램은 active profile에 따라 다른 설정을 사용합니다. 기본적으로 Local 환경에서 실행하기 위한 설정을 README-INSTALL.md에 작성하였습니다. <br/>

## 1. active profile 설정
- application.yml 파일에서 active profile을 설정하거나 intellij에서 active profile을 설정합니다.
  - default : local
- redis cluster 환경의 경우 active profile을 prod로 설정합니다.

## 2. spring-playground 실행
- intellij에서 spring-playground 프로젝트를 실행합니다.
- 실행 순서는 다음과 같습니다.
  1. module-eureka
  2. module-configServer
  3. module-webGateway
  4. module-api/module-api-auth
  5. module-api/module-api-article
  6. module-api/module-api-mail
  7. module-rabbitmq-consumer
 
- IDE 사용하지 않고, Terminal 환경에서 실행시 run-modules.sh를 실행하고 종료를 원할 시 stop-modules.sh를 실행합니다.
- 엔티티 토대로 table 자동생성을 원할 경우, <mark>module-database</mark> 모듈의 application.yml 파일의 hibernate 설정을 변경합니다.
  - spring.jpa.hibernate.ddl-auto: update

## 3. swagger - api 문서 확인
현재 웹 게이트웨이 포트는 8080 입니다.

```html
http://localhost:8080/swagger-ui.html
```
***