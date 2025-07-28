# 📌 CollabEase - 협업툴 기반 게시판 & 실시간 커뮤니케이션 플랫폼

## ✨ 프로젝트 소개

**CollabEase**는 게시판, 태스크 관리, 댓글, 실시간 채팅 기능을 통합한 **협업툴 기반 웹 애플리케이션**입니다. 팀원 간의 효율적인 협업을 위해 작성되었으며, 실시간 의사소통과 업무 추적을 동시에 제공합니다.

> 백엔드는 Spring Boot (Java) 기반, 프론트엔드는 React (TypeScript) 기반으로 구성되어 있습니다.

---

## 📷 주요 기능

| 기능 구분         | 설명 |
|------------------|------|
| 📝 게시판 관리    | 게시글 작성, 수정, 삭제 (소프트 딜리트), 페이지네이션, 조회수 |
| 💬 댓글 시스템    | 대댓글(1-depth), 댓글 작성/삭제, 실시간 반영 |
| 📎 첨부파일 기능   | 게시글과 함께 다중 파일 업로드 지원 |
| ✅ 업무(Task) 관리 | 업무 등록, 상태변경, 기간 관리, 게시글과 연계 |
| 🧑‍🤝‍🧑 OAuth 로그인 | 구글 OAuth2 로그인, 토큰 저장 처리 |
| 📡 실시간 채팅    | WebSocket 기반의 채팅 기능 (예정 또는 구현 포함) |
| 🔔 알림 기능       | 댓글/게시글에 대한 알림 처리 (DB 기반 저장) |

---

## 🧱 기술 스택

### 🔧 백엔드 (Spring Boot)

- Spring Boot 3.x
- Spring Security + JWT + OAuth2 (Google)
- JPA (Hibernate)
- PostgreSQL
- WebSocket (STOMP 기반)
- DTO / Service / Repository 레이어 구조화
- 파일 업로드: Multipart + LocalStorage
- 예외 처리: 커스텀 예외 + GlobalExceptionHandler

### 🎨 프론트엔드 (React + TypeScript)

- React 18
- TypeScript
- MUI (Material UI) + styled-components
- Axios + React Query (또는 Axios 직접 사용)
- React Router v6
- Drag & Drop: @hello-pangea/dnd
- Toast UI Editor / Viewer
- OAuthRedirect + Token 기반 인증 처리

---

## 🧪 실행 방법

### ✅ 백엔드 실행

```bash
# 1. 환경 설정 (.env or application.yml)
spring.datasource.url=jdbc:postgresql://localhost:5432/your-db
spring.datasource.username=your-username
spring.datasource.password=your-password

# 2. 실행
./gradlew bootRun


🗃️ ERD 요약
Member ↔ Role: 다대다 (중간 테이블: MemberRole)

Member → Board: 일대다

Board → Comment: 일대다

Comment → Comment: Self-Join (대댓글)

Board → Attachment: 일대다

Member → Notification: 일대다

Board → Task: 다대일

📎 주요 스크린샷

🙌 기여자
FE / REACT: yourname

BE / SPRING: yourname

DATABASE / 설계: yourname

📌 TODO / 향후 개선 사항
 실시간 채팅 기능 완성 및 UI 통합

 댓글 알림 WebSocket 연동

 게시글/댓글 검색 기능

 관리자 기능 (유저 관리 등)

 CI/CD 파이프라인 (GitHub Actions + Docker)
