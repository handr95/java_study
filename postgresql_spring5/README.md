# 환경 설정

## postgresSQL 설치

* docker run --name postgreSQL -e POSTGRES_PASSWORD=1234 -d -p 5432:5432 postgres
  * docker exec -it postgreSQL bash
  * psql -U postgres
  * create user test with password 1234
  * \c test

* 테이블 생성
  ```postgresql
  CREATE TABLE VEHICLE (
      VEHICLE_NO    VARCHAR(10)    NOT NULL,
      COLOR         VARCHAR(10),
      WHEEL         INT,
      SEAT          INT,
      PRIMARY KEY (VEHICLE_NO)
  );
  ```