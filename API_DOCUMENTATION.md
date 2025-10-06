# API 정의서

## 러닝 스케줄 관리 API

### 1. 러닝 스케줄 등록

#### 기본 정보
- **URL**: `/api/schedules/running`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Description**: 새로운 러닝 스케줄을 등록합니다.

#### 요청 파라미터

```json
{
  "title": "string",           // 제목 (필수)
  "date": "string",            // 날짜 (YYYY-MM-DD 형식, 필수)
  "startTime": "string",       // 시작시간 (HH:mm 형식, 필수)
  "endTime": "string",         // 종료시간 (HH:mm 형식, 필수)
  "nx": "number",             // X 좌표 (필수)
  "ny": "number",             // Y 좌표 (필수)
  "address": "string",        // 주소 (필수)
  "detailLocation": "string"   // 상세장소 (선택)
}
```

#### 요청 예시

```json
{
  "title": "한강 러닝",
  "date": "2024-09-25",
  "startTime": "07:00",
  "endTime": "08:30",
  "x": 127.0276,
  "y": 37.4979,
  "placeName": "한강공원",
  "placeDetail": "여의도 한강공원",
  "placeUrl": "http://...",
  "addressName": "서울특별시 영등포구 여의도동"
}
```

#### 응답

##### 성공 응답 (201 Created)
```json
{
  "id": "string",              // 생성된 스케줄 ID
  "title": "string",           // 제목
  "date": "2024-09-25",        // 날짜
  "startTime": "07:00",        // 시작시간
  "endTime": "08:30",          // 종료시간
  "x": 127.0276,              // X 좌표
  "y": 37.4979,               // Y 좌표
  "placeName": "한강공원",      // 장소명
  "placeDetail": "여의도 한강공원", // 상세장소
  "placeUrl": "http://...",    // 장소 URL
  "addressName": "서울특별시 영등포구 여의도동", // 주소
  "createdAt": "2024-09-24T15:30:00", // 생성일시
  "updatedAt": "2024-09-24T15:30:00"  // 수정일시
}
```

##### 실패 응답 (400 Bad Request)
```json
{
  "error": "VALIDATION_ERROR",
  "message": "에러 메시지"
}
```

#### 에러 코드
- `VALIDATION_ERROR`: 입력값 검증 실패
- `INVALID_DATE_FORMAT`: 날짜 형식 오류
- `INVALID_TIME_FORMAT`: 시간 형식 오류
- `INVALID_COORDINATES`: 좌표값 오류

#### 검증 규칙
- `title`: 1-100자, 필수
- `date`: YYYY-MM-DD 형식, 필수
- `startTime`: HH:mm 형식, 필수 (예: 08:30, 12:00)
- `endTime`: HH:mm 형식, 필수, startTime보다 늦어야 함
- `x`: 숫자, 필수 (경도)
- `y`: 숫자, 필수 (위도)
- `placeName`: 1-100자, 필수 (장소명)
- `placeDetail`: 0-50자, 선택 (상세장소)
- `placeUrl`: 선택 (장소 URL)
- `addressName`: 선택 (주소)

---

## 암호화폐 정보 조회 API

### 1. 특정 암호화폐 실시간 정보 조회

#### 기본 정보
- **URL**: `/api/crypto/{cryptoCodes}`
- **Method**: `GET`
- **Content-Type**: `application/json`
- **Description**: 암호화폐 코드들로 특정 암호화폐들의 실시간 정보를 조회합니다. 업비트 API와 동일한 형식으로 쉼표로 구분하여 여러 종목을 조회할 수 있습니다.

#### 경로 파라미터
- `cryptoCodes`: 암호화폐 코드들 (쉼표로 구분, 예: KRW-BTC,KRW-ETH,KRW-XRP)
  - 공백이 포함되어 있어도 자동으로 제거됩니다
  - 예시: `KRW-BTC, KRW-ETH, KRW-XRP` → `KRW-BTC,KRW-ETH,KRW-XRP`

#### 응답

##### 성공 응답 (200 OK)
```json
{
  "success": true,
  "message": "암호화폐 정보 조회 성공",
  "data": [
    {
      "cryptoCode": "KRW-BTC",
      "cryptoName": "비트코인",
      "tradePrice": "148601000",
      "changePrice": "-136000",
      "changeRate": "-0.09",
      "change": "FALL",
      "openingPrice": "148737000",
      "highPrice": "149360000",
      "lowPrice": "148288000",
      "tradeVolume": "0.00016823",
      "accTradePrice": "31615925234.05438",
      "accTradePrice24h": "178448329314.96686",
      "accTradeVolume24h": "1198.26954807",
      "highest52WeekPrice": "163325000",
      "lowest52WeekPrice": "72100000",
      "tradeTimeKst": "141400",
      "targetPrice": "155,000,000",
      "targetPriceDirection": "DOWN"
    },
    {
      "cryptoCode": "KRW-ETH",
      "cryptoName": "이더리움",
      "tradePrice": "4500000",
      "changePrice": "50000",
      "changeRate": "1.12",
      "change": "RISE",
      "openingPrice": "4450000",
      "highPrice": "4550000",
      "lowPrice": "4400000",
      "tradeVolume": "1.23456789",
      "accTradePrice": "12345678901.23",
      "accTradePrice24h": "98765432109.87",
      "accTradeVolume24h": "1234.56789012",
      "highest52WeekPrice": "5000000",
      "lowest52WeekPrice": "2000000",
      "tradeTimeKst": "141400",
      "targetPrice": null,
      "targetPriceDirection": null
    }
  ]
}
```

##### 실패 응답 (400 Bad Request)
```json
{
  "success": false,
  "message": "암호화폐 코드는 KRW-XXX 형식이어야 합니다. 잘못된 코드: KRW-INVALID"
}
```

#### 사용 예시
- **단일 종목**: `/api/crypto/KRW-BTC`
- **복수 종목**: `/api/crypto/KRW-BTC,KRW-ETH,KRW-XRP`
- **공백 포함**: `/api/crypto/KRW-BTC, KRW-ETH, KRW-XRP` (자동으로 공백 제거됨)

---

## 향후 추가 예정 API

### 2. 러닝 스케줄 조회
- **URL**: `/api/schedules/running`
- **Method**: `GET`
- **Description**: 러닝 스케줄 목록을 조회합니다.

### 3. 러닝 스케줄 상세 조회
- **URL**: `/api/schedules/running/{id}`
- **Method**: `GET`
- **Description**: 특정 러닝 스케줄의 상세 정보를 조회합니다.

### 4. 러닝 스케줄 수정
- **URL**: `/api/schedules/running/{id}`
- **Method**: `PUT`
- **Description**: 기존 러닝 스케줄을 수정합니다.

### 5. 러닝 스케줄 삭제
- **URL**: `/api/schedules/running/{id}`
- **Method**: `DELETE`
- **Description**: 러닝 스케줄을 삭제합니다.
