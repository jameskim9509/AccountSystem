# AccountSystem
 
계좌 api 시스템 개발
( 사용자id가 저장된 데이터베이스가 존재한다고 가정 )

- 요구사항 (HTTP API 방식으로 요청)
1. 계좌 추가(생성), 해지, 확인 및 계좌 정보 처리 
※ 계좌 추가는 사용자당 최대 10개까지 가능
2. 잔액 사용, 잔액 사용 취소, 거래 확인 및 관련된 계좌 및 거래 처리 

- 기술 요구사항
1. JPQL, JPA Auditing를 이용한 Entity 생성 및 관리, Database 설계
2. Embeded Redis 서버와 AOP 기술을 이용한 중복된 거래 방지(동시성 제어 - Lock, unLock)
3. 커스텀 에러, 에러 핸들러 및 에러코드를 통해 잘못된 정보 입력에 대해 일관성 있는 예외 처리
4. Junit5를 통한 각각의 상황에 대한 단위 테스트 (by Mock Object)
5. Controller, Service, Repository의 Layered Architecture를 통한 비즈니스 로직 처리

- Spring boot Application 개발 환경
intellij IDE
내장 tomcat
내장 h2 Database
Postman

- 패키지 구조 <br>
aop : 락을 이용한 거래 중복 방지시 AOP 관련 코드 <br>
config : Redis 서버 환경 구성 정보 관련 코드 <br>
controller : RestController를 통한 다양한 요청(get, post, delete)에 대한 json 응답 코드 <br>
domain : transaction 테이블, user 테이블, account 테이블 등 엔티티 관련 코드 <br>
dto : controller 요청 및 응답 처리시 전달 객제(Data Transfer Object) 관련 코드 <br>
exception : 커스텀 예외 및 예외 처리 핸들러 관련 코드 <br>
repository : DB연결 및 정보 저장, 수정 코드 <br>
service : 비즈니스 로직과 관련한 코드 <br>
type : 에러코드 및 Enum 타입 관련 코드 <br>

- DB 테이블 <br>
<img src = "/DB_capture.PNG">
<br>

- POSTMAN 사용 예 <br>
<img src = "/use_case_1.PNG" width="600" height="600">
<br>
