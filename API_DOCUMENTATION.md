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
