# Major Runner

 모든 전공인들이 함께 달려가는 공간, Major Runner
 
같은 꿈을 안고 있는 대학생, 취업 준비생들이 모여 정보를 공유하고, 스터디를 모집하며, 질의응답 등을 나눌 수 있는 공간입니다.   











------

## 라이브러리    
 



\-    maven 프로젝트 / java 1.8 

\-    Spring Boot 2.1.17

\-    Spring JPA

\-    Spring HATEOAS

\-    Spring WEB

\-    Spring devtools

\-    Lombok

\-    Spring Security

\-    mysql-connector 
  


------


## 테스트 라이브러리    




\-    Spring test (test)

\-    Spring Security test (test)

\-    Spring RestDocs (test)

\-    junit (test)

\-    h2 database (test)

 ------

## 폴더 구조

```

major-server
 ├─ README.md
 ├─ pom.xml
 ├─ src
 │  ├─ main
 │  │  ├─ java
 │  │  │   ├─ com.majorrunner.majorserver 
 │  │  │   │   ├─ account
 │  │  │   │   ├─ accountInfoCategory
 │  │  │   │   ├─ accountInfo
 │  │  │   │   ├─ category
 │  │  │   │   ├─ comment
 │  │  │   │   ├─ like
 │  │  │   │   ├─ post
 │  │  │   │   ├─ common
 │  │  │   │   └─ config
 │  │  │   └─ MajorServerApplication.java
 │  │  └─ resources
 │  │      ├─ static
 │  │      ├─ templates
 │  │      └─ application.properties
 │  └─ test                                 // test code 
 │     ├─ java
 │     │   └─ com.majorrunner.majorserver
 │     │       ├─ post      
 │     │       └─ ...
 │     └─ resources
 │         └─ application.properties
 ├─ target
 └─ ...
```



## Docker



**docker와 mysql**

1. docker 설치

   [https://docs.docker.com/get-docker/]: 

   

2. mysql Docker 이미지 다운로드

```
  docker pull mysql:8.0.17  
```

 

3. Docker mysql 컨테이너 생성 및 실행

```
docker run -d -p 3306:3306 -e  MYSQL_ROOT_PASSWORD=(컨테이너 비밀번호) --name (컨테이너 이름) mysql  
```

 

4. 컨테이너 동작 확인



```
  docker ps -a
```

  

5. shell에서 mysql 컨테이너 접속



```
  docker exec -i -t (컨테이너 이름) bash
```

  



6. mysql root 계정의 password를 입력하여 mysql 서버에 접속

```
  mysql -u root -p;  
```

 

7. database 생성 후 진행

> application.properties에 mysql관련 DB설정을 넣은 후 프로젝트 실행하여 DB 연동을 확인한다.

 

도커 실행 ,재실행, 접속, 종료

```null
docker start [컨테이너 id 또는 name]
```

```null
docker restart [컨테이너 id 또는 name]
```

```null
docker attach [컨테이너 id 또는 name]
```

```null
docker stop [컨테이너 id 또는 name]
```




------


## DB 구조 


![db1](https://user-images.githubusercontent.com/26570275/93857245-48653200-fcf5-11ea-9bfa-8f73653bf283.png)



------
## Check list
------


 **User 기능**
 
- [x] 회원 가입 
- [x] 회원 조회
- [x] 회원 탈퇴 
- [x] 회원 정보 수정

 **UserInfo 기능**  
 
- [x] userinfo 생성 
- [x] userinfo 삭제 

 **Post 기능**

- [x] post 생성 
- [x] post 수정 
- [x] post 삭제 
- [x] post 조회 
- [x] post paging 조회
- [x] 좋아요 기능
- [x] 조회수 증가
- [x] comment 추가

 **Comment 기능**
 
- [x] comment 수정
- [x] comment 삭제

 **Like 기능**
 
- [x] like 생성 
- [x] like 삭제 
