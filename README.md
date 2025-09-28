# Tskim Portfolio Backend

## 📋 프로젝트 개요
개인 포트폴리오 웹사이트의 백엔드 API 서버입니다.

## 🚀 기술 스택

### **Backend Framework**
- **Java**: JDK 21 LTS
- **Spring Boot**: 3.2.0
- **Build Tool**: Gradle 8.5

### **Database**
- **Database**: MongoDB Atlas (Cloud)
- **ODM**: Spring Data MongoDB
- **Connection**: MongoDB Atlas Connection String

### **Security & Authentication**
- **Spring Security**: 6.2.0
- **JWT**: JSON Web Token (jjwt 0.11.5)

### **Documentation**
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger UI)

### **Monitoring**
- **Spring Boot Actuator**: 헬스체크 및 모니터링

## ☁️ 배포 환경

### **Hosting Service**
- **Web Service**: Render Web Service
- **Database**: MongoDB Atlas (Cloud)

### **환경변수**
```bash
MONGODB_URI=mongodb+srv://[username]:[password]@[cluster].mongodb.net/[database]
WEATHER_API_KEY=your_weather_api_key_here
PORT=8080
```

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/tskim/portfolio/
│   │   ├── StartApplication.java          # 메인 애플리케이션 클래스
│   │   ├── config/                        # 설정 클래스들
│   │   │   ├── AppConfig.java
│   │   │   └── SecurityConfig.java        # Spring Security 설정
│   │   ├── controller/                    # REST API 컨트롤러
│   │   │   ├── BaseController.java
│   │   │   └── TestController.java        # MongoDB 연결 테스트
│   │   ├── dto/                          # 데이터 전송 객체
│   │   │   ├── BaseDto.java
│   │   │   └── PortfolioProjectDto.java
│   │   ├── entity/                       # MongoDB Document
│   │   │   ├── BaseDocument.java
│   │   │   └── PortfolioProject.java
│   │   ├── repository/                    # MongoDB Repository
│   │   │   ├── BaseMongoRepository.java
│   │   │   └── PortfolioProjectRepository.java
│   │   ├── service/                      # 비즈니스 로직
│   │   │   └── BaseService.java
│   │   └── scheduler/                     # 스케줄러 (예정)
│   └── resources/
│       ├── application.yml               # 애플리케이션 설정
│       └── (MongoDB 설정은 application.yml에 직접 포함)
└── test/
    └── java/com/tskim/portfolio/
        └── PortfolioApplicationTests.java
```

## 🛠️ 개발 환경 설정

### **필수 요구사항**
- JDK 21 LTS
- Gradle 8.5 (Wrapper 포함)
- MongoDB Atlas 계정
- 기상청 API 키 (공공데이터포털)

### **MongoDB Atlas 설정**
1. MongoDB Atlas에서 클러스터 생성
2. 데이터베이스 사용자 생성
3. **IP 화이트리스트 설정** (중요!)
4. 연결 문자열 복사

#### **IP 화이트리스트 설정 방법**
- 렌더 서버에 배포 후 몽고템플릿은 동작하는데 몽고리포지터리는 ssl 어쩌고 타임아웃 문제가 발생함.
1. MongoDB Atlas 콘솔 → **Network Access** → **IP Access List**
2. **Add IP Address** 클릭
3. 0.0.0.0/0 추가하니 해결됨
4. 렌더서버 페이지의 Events 메뉴에서 우측상단에 Connect 버튼누르면 Outbound IP Addresses 있음 0.0.0.0/0 대신에 이거 추가하면 됨
5. **Save** 클릭

**⚠️ 주의사항:**
- Render 서버는 동적 IP를 사용하므로 `0.0.0.0/0` 설정이 필요합니다
- IP 화이트리스트 미설정 시 SSL 연결 에러가 발생합니다

### **로컬 실행**
```bash
# 의존성 다운로드 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### **API 엔드포인트**
- **홈페이지**: `http://localhost:8080/`
- **MongoDB 테스트**: `http://localhost:8080/api/test/mongodb`
- **헬스체크**: `http://localhost:8080/actuator/health`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 📦 주요 의존성

### **Spring Boot Starters**
- `spring-boot-starter-web`: 웹 애플리케이션
- `spring-boot-starter-data-mongodb`: MongoDB 데이터 액세스
- `spring-boot-starter-validation`: 데이터 검증
- `spring-boot-starter-security`: 보안
- `spring-boot-starter-actuator`: 모니터링

### **JWT**
- `jjwt-api`: JWT API
- `jjwt-impl`: JWT 구현체
- `jjwt-jackson`: JWT Jackson 지원

### **Documentation**
- `springdoc-openapi-starter-webmvc-ui`: Swagger UI

### **Development**
- `lombok`: 코드 생성 라이브러리

## 🔧 설정 정보

### **데이터베이스 설정**
- **로컬/프로덕션**: MongoDB Atlas (Cloud)
- **연결**: MongoDB Atlas Connection String
- **Auditing**: 자동 생성/수정 시간 추적

### **보안 설정**
- **CSRF**: 비활성화 (API 서버)
- **인증**: JWT 기반
- **허용 엔드포인트**: `/`, `/api/**`, `/actuator/**`

### **포트 설정**
- **로컬**: 8080
- **프로덕션**: 환경변수 `PORT` 사용

## 🚀 배포 정보

### **Render 배포 설정**
1. **Web Service** 생성
2. **환경변수** 설정:
   - `MONGODB_URI`
   - `WEATHER_API_KEY`
   - `PORT`
3. **MongoDB Atlas IP 화이트리스트**: `0.0.0.0/0` 설정 필수
4. **자동 배포**: GitHub 연동

#### **배포 시 주의사항**
- **MongoDB Atlas IP 화이트리스트**에 `0.0.0.0/0` 추가 필수
- 미설정 시 `SSLException: Received fatal alert: internal_error` 에러 발생
- Render 서버는 동적 IP를 사용하므로 모든 IP 허용이 필요합니다

### **빌드 명령어**
```bash
./gradlew build
```

### **실행 명령어**
```bash
java -jar build/libs/tskim-portfolio-backend-0.0.1-SNAPSHOT.jar
```

## 📝 개발 노트

### **패키지 구조**
- **Base Package**: `com.tskim.portfolio`
- **도메인별 패키지**: `controller`, `service`, `repository`, `entity`, `dto`

### **코딩 컨벤션**
- **Java**: 표준 Java 네이밍 컨벤션
- **Spring**: Spring Boot 베스트 프랙티스
- **MongoDB**: Document는 단수형, Collection은 복수형

### **MongoDB 특징**
- **유연한 스키마**: 동적 필드 추가 가능
- **JSON 형태**: 직관적인 데이터 구조
- **확장성**: 클라우드 기반 자동 스케일링

---
*이 문서는 프로젝트 개발 과정에서 지속적으로 업데이트됩니다.*
