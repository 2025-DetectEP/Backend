# Pribee : SNS 개인정보 탐지 보조 서비스

---

### ▶️ 기술 스택

- **Java 17**
- **Spring Boot 3.4.2**
    - Spring Data JPA (MySQL)
    - Spring Data MongoDB
    - Spring Data Redis
    - Spring Validation
    - Spring Web
- **Swagger (SpringDoc OpenAPI)**
- **Lombok**
- **JUnit 5 (테스트 프레임워크)**

---

### ▶️ 코드 스타일

- naver code convention

---

### ▶️ Github 규칙

- **브랜치**
    - main: 운영 배포 브랜치
    - develop: 개발 브랜치
    - KAN-1/feat/{기능명}: 새로운 기능 개발
    - KAN-1/fix/{버그명}: 긴급 버그 수정
    - KAN-1/chore/{작업내용}: CI/CD 및 기타 설정 변경

- **커밋 메시지 규칙**
  | 타입 | 설명 | 예시 |
  |------|------|------|
  | `feat` | 새로운 기능 추가 | `feat: 회원가입 API 구현` |
  | `fix` | 버그 수정 | `fix: 로그인 오류 해결` |
  | `refactor` | 코드 리팩토링 | `refactor: JWT 토큰 검증 로직 개선` |
  | `docs` | 문서 수정 | `docs: README 코드 스타일 가이드 추가` |
  | `test` | 테스트 코드 추가 및 변경 | `test: 유저 서비스 단위 테스트 추가` |
  | `chore` | 기타 변경 사항 | `chore: Gradle 의존성 업데이트` |

---

