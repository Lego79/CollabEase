# Random Learning Challenge Tool

## Overview

The **Random Learning Challenge Tool** is an AI-powered platform that delivers unexpected, random learning challenges to users every week. The challenges span a wide variety of topics, from technical skills to quirky hobbies, encouraging users to step out of their comfort zones and explore something new. Whether it’s learning how to read tarot cards or analyzing the running patterns of ostriches, users can have fun while developing new skills!

## Features

### 1. Random Challenge Generator
- **AI-Generated Challenges**: Every week, users receive a unique learning challenge chosen by an AI. These challenges can range from programming tasks to creative or quirky hobbies.
- **Personalization**: Challenges can be tailored based on user preferences, past interactions, and completion history.

### 2. Community Interaction
- **Share Your Progress**: Users can post their progress, results, and insights from their challenges on the community board.
- **Feedback and Engagement**: Commenting, liking, and feedback mechanisms allow users to interact with each other’s submissions.
- **Showcase**: Users can showcase their work (e.g., videos, images, or written summaries).

### 3. Progress Tracking
- **Challenge Completion Tracking**: Users can track which challenges they’ve completed and what they learned from each one.
- **Badges & Rewards**: Earn badges for completing challenges, unlocking special badges for completing a series of challenges or themed challenges.

### 4. Fun and Quirky Challenges
- **Serious and Silly Mix**: Alongside practical learning challenges, the platform includes humorous and unconventional tasks (e.g., “Set your alarm to a chicken sound for a week”).
- **Random Challenge Mode**: Let the AI surprise you with the most unexpected learning challenges!

### 5. Certification System
- **Challenge Certification**: Upload photos, videos, or documents as proof of challenge completion. Certifications are visible to other users in your profile.

### 6. Thematic Challenges
- **Monthly Themes**: Every month, the platform introduces a new theme (e.g., ‘Creative Arts’ or ‘STEM Exploration’) with relevant learning challenges.
- **Special Events**: Themed events and competitions where users can participate and earn exclusive rewards.

### 7. Friends and Competitions
- **Invite Friends**: Collaborate or compete with friends by tackling the same challenges.
- **Leaderboard**: Track progress against others on global and friend leaderboards.

## Getting Started

### Prerequisites
- **Node.js**: Make sure you have Node.js installed (>= 14.x).
- **PostgreSQL**: The platform requires a PostgreSQL database to store user data and challenge details.
- **AI Integration**: You will need access to an AI API for generating random challenges (e.g., OpenAI's GPT).

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/username/random-learning-challenge-tool.git
   cd random-learning-challenge-tool


1. 프로젝트 구조 설계 및 기본 세팅
   프로젝트 생성: Spring Initializr 등을 통해 Spring Boot 프로젝트 생성. Maven 또는 Gradle 설정, 주요 의존성(웹, JPA, Spring Security, Lombok 등) 추가.
   패키지 구조 설계: controller, service, repository, domain, aop(필요 시), config 등 공통적으로 사용하는 계층별 구조를 잡아두면 이후 확장 시 편리하다.
   DB 연결 설정: application.yml 혹은 application.properties 파일에서 Datasource 설정. 초반에는 In-Memory DB(H2 등)로 빠르게 시작할 수 있다.
   참고 자료:

Spring Boot Reference Guide
Spring Framework AOP Guide
2. 엔티티(Entity) 및 도메인 모델 정의
   게시글(Board) 엔티티: 게시글 ID(가능하면 UUID), 제목, 내용, 작성자, 작성일, 수정일 등의 필드를 설계한다.
   작성자(User) 엔티티: 사용자 ID(UUID), 이름, 이메일, 권한(Role) 등을 설정. 추후 인증 인가 처리를 위해 필요하다.
   파일 첨부(Attachment) 엔티티 (선택적): 게시글과 1:N 관계를 구성해서 파일 업로드 기능을 지원한다면 테이블 분리를 고려한다.
   의도:

UUID를 사용해 주 키를 관리하면 식별성이 좋아 중복 가능성을 최소화할 수 있다.
게시글-첨부파일 관계, 게시글-작성자 관계를 명확히 설계해두면 이후 기능 확장에 유리하다.
3. 인증과 인가 (Spring Security)
   Spring Security 설정: WebSecurityConfigurerAdapter 또는 SecurityConfig 파일을 생성해 HTTP 접근 권한과 인증/인가 로직을 설정한다.
   UserDetailsService 구현: DB의 사용자 정보를 Spring Security에서 인식할 수 있도록 커스텀 구현하여 인증 절차를 구성한다.
   JWT(선택 사항): 세션 방식이 아니라 토큰 기반 인증을 선호한다면 JWT를 사용해 무상태(Stateless) 아키텍처를 구성한다.
   의도:

인증/인가를 일관된 구조로 관리하기 위해 Spring Security 사용.
API 호출 시 권한(Role)에 따라 접근을 제한하기 용이.
4. 예외 처리 및 AOP 활용
   예외 처리 전략 수립: 예상 가능한 예외를 커스텀 Exception 클래스로 정의하고, ControllerAdvice와 ExceptionHandler를 이용해 일관적으로 처리한다.
   HTTP Status 코드 활용: 예외 상황에 맞는 HttpStatus를 적절히 매핑하여 클라이언트 측에서 상황을 명확히 파악하도록 한다.
   AOP를 통한 로깅: 중복된 try-catch 블록을 대체하거나, 공통 로직(메서드 진입/종료 로깅, 예외 발생 시 후처리 등)을 AOP로 분리한다.
   의도:

중복되는 try-catch 제거와 로깅은 AOP를 통해 해결할 수 있다.
예외 유형별로 명시적으로 설계해 코드 가독성을 높이고 유지보수를 용이하게 만든다.
5. Controller, Service, Repository 구현
   Controller
   게시글 등록/수정/삭제/조회, 파일 업로드, 다운로드 등 REST API를 설계한다.
   요청이 들어오면 인증/인가 체크 후 적절한 Service를 호출한다.
   Service
   비즈니스 로직 담당. 트랜잭션 관리와 예외 처리를 수행한다.
   파일 업로드 시 실제 저장 경로 관리, 데이터 파싱 및 변환, DB 레포지토리 호출 등의 로직을 처리.
   Repository (Spring Data JPA 가정)
   JpaRepository를 상속하여 엔티티별 CRUD 메서드를 사용한다.
6. 파일 IO 처리
   파일 저장: DB의 Blob/Clob 대신 로컬 디렉토리나 Cloud Storage(예: AWS S3)에 저장하고, 저장 경로를 DB에 보관하는 방법이 일반적이다.
   파일 다운로드: 저장된 파일의 Path 정보를 바탕으로 파일을 내려주거나 리소스 링크를 제공한다.
   정책: 파일 이름 중복 처리, 확장자 필터링, 용량 제한, 보안 이슈 등을 사전에 설계한다.
7. 로그 및 모니터링
   로그 출력(AOP 활용): 원하는 패키지/메서드 범위를 지정해 로깅 로직을 자동으로 적용. (메서드 진입/반환/예외 상황 등)
   Spring Boot Actuator 활용: 서버 상태, 헬스체크, 메트릭 등 모니터링 기능을 제공한다.
8. 웹플럭스(리액티브 프로그래밍) (선택 사항)
   Spring WebFlux: 고성능, 비동기 처리에 적합한 리액티브 모델. Reactor 기반으로 동작한다.
   적용 시점 고려: 게시판 구현이 주로 동기적 프로세스라면 MVC로 충분하지만, 대규모 트래픽이나 비동기 처리가 많다면 WebFlux도 고려한다.
9. 테스트 및 배포
   단위 테스트: JUnit, Mockito 등을 사용해 Controller, Service 계층 테스트.
   통합 테스트: SpringBootTest 등을 활용해 DB와 연동까지 확인.
   빌드 및 배포: Docker 등을 사용해 컨테이너화 후, 원하는 서버나 클라우드 환경에 배포. 운영 로그 및 예외 모니터링 체계 구축.
10. 유지보수 및 기능 확장
    공통 모듈화: 작성일/수정일, 작성자 정보를 자동으로 주입하거나, 로깅, 예외 처리를 공통화해 재사용도를 높인다.
    확장 포인트: 게시판을 다양하게 변형(예: 카테고리, 권한별 접근 제한, 공지사항 자동 상단 고정 등).
    버전 관리: API 버전을 분리하거나 Swagger, Spring REST Docs 등을 통해 문서화하여 유지보수성을 높인다.
    요약
    초급 단계에서는 단순 CRUD와 파일 저장, 기본 예외 처리 정도만 구현한다.
    중급 이상의 구현에서는 AOP를 사용해 공통 로직을 분리하고, 커스텀 예외 클래스와 일관된 예외 처리, 보안(인증/인가)을 체계적으로 도입한다.
    데이터 직렬화/역직렬화(JSON) 시 Jackson 등 프로퍼티와 MessageConverter 설정을 적절히 다뤄서 확장성을 높인다.
    Q1

이 구조를 유지하면서 게시판 기능을 모듈화하여 다른 프로젝트에서 재활용하려면 어떻게 설계해야 할까?

Q2

파일 업로드 및 다운로드 시 성능을 최대화하거나 보안을 강화하기 위해 어떤 구체적 기법을 적용할 수 있을까?

Q3

예외 처리를 세밀하게 분류할 때, 발생 가능한 시나리오와 그에 따른 예외 계층 설계는 어떻게 하면 좋을까?