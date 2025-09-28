# MongoDB Atlas 연결 설정 가이드

## 1. MongoDB Atlas에서 연결 정보 확인

1. MongoDB Atlas 콘솔에 로그인
2. 프로젝트 선택 → Cluster 선택
3. "Connect" 버튼 클릭
4. "Connect your application" 선택
5. Connection String 복사

## 2. application.yml 파일 설정

`src/main/resources/application.yml` 파일에서 MongoDB URI는 환경변수를 사용합니다:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}
```

## 3. 연결 테스트

애플리케이션 실행 후 다음 URL로 연결 테스트:
```
GET http://localhost:8080/api/test/mongodb
```

성공 응답 예시:
```json
{
  "status": "success",
  "message": "MongoDB 연결 성공",
  "database": "your-database-name",
  "timestamp": 1234567890123
}
```

## 4. 보안 주의사항

- MongoDB 연결 정보와 API 키는 환경변수로 관리합니다
- `application.yml`에는 환경변수 참조만 포함되어 있습니다
- 프로덕션 환경에서는 반드시 환경변수 사용을 권장합니다
- MongoDB Atlas에서 IP 화이트리스트 설정을 확인하세요

**환경변수 설정 방법은 README.md를 참조하세요.**
