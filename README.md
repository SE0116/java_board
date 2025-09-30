# java_board

## 기술 스택
- JDK 21
- Spring boot 3.2.12
- Gradle
- PostgreSQL 16
- MyBatis
- Thymeleaf
- jquery

## 프로젝트 구조
```
src/
├─ main/
│   ├─ java/sy/board/
│   │   ├─ controller/PostController.java
│   │   ├─ domain/Post.java
│   │   ├─ mapper/PostMapper.java
│   │   ├─ service/PostService.java
│   │   └─ support/PageParam.java
│   └─ resources/
│       ├─ mapper/PostMapper.xml
│       └─ templates/
│           ├─ layout.html
│           ├─ posts/
│           │   ├─ list.html
│           │   ├─ form.html
│           │   └─ view.html
│           └─ static/css/main.css
└─ test/...
```

## 🏗️ 프로젝트 구조 및 기능 구현 흐름

### 1. 프로젝트 초기화
- Spring Initializr로 Gradle + Java + Spring Boot 3.4.10 프로젝트 생성 후 3.2.12로 다운그레이드
- Dependencies:
    - Spring Web (웹 MVC 지원)
    - Thymeleaf (템플릿 엔진)
    - MyBatis Framework (DB 매퍼)
    - PostgreSQL Driver (DB 연동)
    - Validation (입력값 검증)

### 2. 기본 도메인 & 서비스 계층
- `Post` 엔티티 작성 (id, title, author, content, viewCount, createdAt, updatedAt)
- `PostMapper.xml` 및 `PostMapper.java` 작성
- `PostService` 작성하여 CRUD 및 조회수 증가 처리

### 3. 컨트롤러
- `PostController.java`
    - `/posts` 목록 조회 (페이지네이션 포함)
    - `/posts/new` 새 글 작성 폼
    - `POST /posts` 글 저장
    - `GET /posts/{id}` 글 상세 보기 (조회수 증가)
    - `GET /posts/{id}/edit` 글 수정 폼
    - `PUT /posts/{id}` 글 수정
    - `DELETE /posts/{id}` 글 삭제

### 4. 뷰 (Thymeleaf Templates)
- `list.html` : 게시글 목록 + 페이지네이션
- `form.html` : 글쓰기 및 수정 폼
- `view.html` : 게시글 상세 보기 + 댓글 영역
- `layout.html` : 공통 레이아웃 (헤더, 네비게이션)

### 5. 페이지네이션 개선
- 기존 "이전 / 다음" 방식 → 숫자 페이지 전환 방식
- 10페이지 초과 시, 현재 페이지 기준 앞뒤 5개만 표시
- 앞/뒤 페이지 부족 시, 남는 공간을 반대쪽 페이지 숫자로 채움

### 6. 다중 선택 삭제
- 목록에 체크박스 추가
- 선택한 게시글들을 한 번에 삭제 가능

### 7. 검색/필터링 기능
- 목록 상단에 검색창 추가
- 제목, 내용, 작성자에 검색 키워드 포함 시 필터링

---

## ✅ 현재 상태
- 간단한 CRUD
- 페이지네이션
- 검색 및 필터링
- 뷰 단일화 (Thymeleaf 기반)

---

## 📖 참고
- [Spring Boot 공식 문서](https://docs.spring.io/spring-boot/docs/3.2.12/reference/html/getting-started.html#getting-started)
- [Thymeleaf 공식 문서](https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html)
