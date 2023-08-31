
### 주문관리 API 서버


- Gradle을 활용해 프로젝트에 필요한 라이브러리 설정
- spring-security 기반으로 JWT 인증 및 인가 처리 구현

- 개발에 필요한 데이터베이스 스키마 및 샘플 데이터가 준비되 있습니다.
  * In-Memory 경량 RDBMS H2가 사용됩니다. **프로젝트를 재시작 할 때마다 데이터가 초기화 됩니다.**
  * 스키마: resources/schema-h2.sql
  * 샘플 데이터: resources/data-h2.sql
  * 샘플 데이터에는 `ID: tester@gmail.com / 비밀번호: test!1` 사용자가 초기 셋팅되 있습니다.

> 특히 JwtAuthenticationFilter 클래스는 HTTP 요청헤더에서 JWT 값을 추출하고 해당 값이 올바르다면 인증된 사용자 정보(JwtAuthenticationToken)를 SecurityContextHolder에 set 합니다.
> RestController에서는 @AuthenticationPrincipal 어노테이션을 사용하여 인증된 사용자 정보(CustomUser)에 접근할 수 있습니다.

---

### API 응답 포맷

정상처리 및 오류처리에 대한 API 서버 공통 응답 포맷을 아래와 같이 정의 합니다.

- 정상처리 및 오류처리 모두 success 필드를 포함합니다.
  * 정상처리라면 true, 오류처리라면 false 값을 출력합니다.
- 정상처리는 data 필드를 포함하고 error 필드는 null 입니다.
  * 응답 데이터가 `단일 객체`라면, data 필드는 `JSON Object`로 표현됩니다.
  * 응답 데이터가 `스칼라 타입(string, int, boolean)`이라면, data 필드는 `string, int, boolean로 표현`됩니다.
  * 응답 데이터가 `Collection`이라면, data 필드는 `JSON Array`로 표협됩니다.
- 오류처리는 error 필드를 포함하고 data 필드는 null 입니다. error 필드는 status, message 필드를 포함합니다.
  * status : HTTP Response status code 값과 동일한 값을 출력해야 합니다.
  * message : 오류 메시지가 출력 됩니다.

#### 로그인 성공 응답 예시

```json
{
    "success": true,
    "data": {
        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoicHJvZ3JhbW1lcnMiLCJuYW1lIjoidGVzdGVyIiwiaWF0IjoxNjExMTQxMjMxLCJ1c2VyS2V5IjoxfQ.XG9ehe1-Q0kWGRhJTi5pWQ-D6ymA5aMKGV7I0qnT9lkz1end0FuEByixkUNEgGY3yiCZiAh380fnf9Q38SiEzw",
        "user": {
            "name": "tester",
            "email": "tester@gmail.com",
            "loginCount": 1,
            "lastLoginAt": "2023-08-20 20:13:51",
            "createAt": "2023-08-20 20:13:36"
        }
    },
    "error": null
}
```

#### 로그인 실패 응답 예시

- 로그인 아이디 누락 (HTTP STATUS 400)
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "principal must be provided",
    "status": 400
  }
}
```
- 로그인 아이디/비밀번호 미일치 (HTTP STATUS 401)
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "Bad credential",
    "status": 401
  }
}
```

### 공개용 API 및 인증 사용자용 API 구분

API는 사용자가 로그인하지 않아도 호출할 수 있는 `공개용 API`와 로그인 후 호출할 수 있는 `인증 사용자용 API`로 구분됩니다.

- 공개용 API
  * 로그인: /api/users/login
  * 단일 상품조회: /api/products/{id}
  * 상품 목록조회: /api/products
- 인증 사용자용 API
  * 내 정보 조회: /api/users/me
  * 주문 리뷰작성: /api/orders-details/{id}/review
  * 주문 배송처리: /api/orders/{id}/orders-details/{orderDetailId}/shipping
  * 주문 완료처리: /api/orders/{id}/orders-details/{orderDetailId}/complete
  * 주문 거절처리: /api/orders/{id}/orders-details/{orderDetailId}/reject
  * 단일 주문조회: /api/orders/{id}
  * 주문 목록조회: (HTTP.GET) /api/orders
  * 상품 주문: (HTTP.POST) /api/orders

로그인 응답에는 `token` 필드가 포함되 있습니다.
```json
{
  "success": true,
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...이하생략...",
    "user": {
      "name": "tester",
      ...생략...
    }
  },
  "error": null
}
```
`인증 사용자용 API`를 호출하기 위해 요청 헤더에 `Authorization` 항목을 추가하고, 값으로 로그인 후 전달받은 `token`에 `Bearer` 키워드를 앞에 붙여 입력합니다.
```
curl --request GET 'http://localhost:8080/api/users/me' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...이하생략...' 
```

인증 사용자용 API 호출시 `Authorization` 헤더가 누락되거나 값이 올바르지 않다면 아래와 같은 오류 응답이 발생합니다.
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "Unauthorized",
    "status": 401
  }
}
```


#### 주문 API

상품 목록에 나와 있는 상품을 주문한다. (복수 선택가능)

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.orders.orders.ReviewRestController
- 컨트롤러 메소드명: review
- URL: POST /api/orders-details/{id}/review
  * {id}: 리뷰를 남기려는 주문의 PK
- Request Body: 리뷰 내용
```json
{
  "content": "review test"
}
```


#### 주문 리뷰 작성 API

인증된 사용자 본인의 주문에 대해 리뷰를 작성한다.

주문 상태 `state`가 `COMPLETED`라면 리뷰를 작성할 수 있다. 단, 동일한 주문에 대해 중복 리뷰를 작성할 수 없다. 정상적으로 리뷰가 작성되면 리뷰 대상 `Product`의 `reviewCount` 값이 1 증가한다.

리뷰 작성이 불가능하다면 `400 오류`를 응답한다.

자신의 주문이 아닌 경우 `404 오류`를 응답한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: roder
- URL: POST /api/orders
- Request Body: 리뷰 내용
  * Request Body 중 orderPrice 필드와 orderProducts 필드는 필수 입력값이다.
```json
{
  "orderPrice": 0,
  "orderProducts": [
    {
      "productId": 10,
      "quantity": 1,
      "price": 19900
    },
    {
      "productId": 8,
      "quantity": 1,
      "price": 57500
    }
  ]
}
```
- Response Body: 작성된 리뷰 내용
```json
{
  "success": true,
  "data": {
    "seq": 2,
    "productId": 3,
    "content": "review test",
    "createAt": "2021-01-20 20:16:47"
  },
  "error": null
}
```
- 중복 리뷰 작성 오류 응답 예시
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "Could not write review for orders 4 because have already written",
    "status": 400
  }
}
```
- 주문 상태 `state`가 `COMPLETED`가 아닌 경우 오류 응답 예시
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "Could not write review for orders 1 because state(REQUESTED) is not allowed",
    "status": 400
  }
}
```
- 자신의 주문이 아닌 경우
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "Could not found orders for 1",
    "status": 404
  }
}
```

#### 주문 목록 조회 API

인증된 사용자 본인의 주문 목록을 출력한다.

주문은 `Review`를 포함할 수 있다. (샘플 데이터에서 `seq=4`인 주문은 리뷰를 포함한다.)

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: getOrders
- URL: GET /api/orders?page=0&size=10&sort=createAt%2CDESC
  * page: page 기반 페이징 처리 파리미터 (최소값: 0, 최대값: Long.MAX_VALUE, 기본값: 0)
  * size: 출력할 아이템의 갯수 (최소값 1, 최대값: 10, 기본값: 5)
  * sort: 출력할 아이템의 정렬 기준 (기본값: 최근 작성일)
  * page, size 값이 최소값~최대값 범위 밖이거나 주어지지 않는다면 기본값으로 대체
  * Response Header 'x-pagination' 항목에 page 정보가 포함된다
- Response Body: 주문 내용 목록 (아래 출력 예시는 page=0, size=5 인 경우)
- page=1, size=5 인 경우 order id=4 부터 출력된다.

```json
{
  "success": true,
  "data": [
    {
      "id": 9,
      "details": [
        {
          "id": 11,
          "product": {
            "id": 7,
            "name": "여행용 파우치",
            "reviewCount": 0,
            "createAt": "2023-08-08 22:37:29"
          },
          "productName": "여행용 파우치",
          "price": 17900,
          "quantity": 1,
          "state": "REQUESTED"
        }
      ],
      "totalAmount": 17900,
      "createAt": "2023-08-31 13:58:22"
    },
    {
      "id": 8,
      "details": [
        {
          "id": 10,
          "product": {
            "id": 6,
            "name": "종이컵(소)",
            "reviewCount": 0,
            "createAt": "2023-08-08 22:37:29"
          },
          "productName": "종이컵(소)",
          "price": 10000,
          "quantity": 10,
          "state": "REQUESTED"
        }
      ],
      "totalAmount": 10000,
      "createAt": "2023-08-31 13:52:03"
    },
    {
      "id": 7,
      "details": [
        {
          "id": 9,
          "product": {
            "id": 11,
            "name": "노트북 받침대",
            "reviewCount": 0,
            "createAt": "2023-08-31 01:25:27"
          },
          "productName": "노트북 받침대",
          "price": 38800,
          "quantity": 1,
          "state": "REQUESTED"
        }
      ],
      "totalAmount": 38800,
      "createAt": "2023-08-31 13:51:29"
    },
    {
      "id": 6,
      "details": [
        {
          "id": 8,
          "product": {
            "id": 7,
            "name": "여행용 파우치",
            "reviewCount": 0,
            "createAt": "2023-08-08 22:37:29"
          },
          "productName": "여행용 파우치",
          "price": 17900,
          "quantity": 1,
          "state": "REQUESTED"
        }
      ],
      "totalAmount": 17900,
      "createAt": "2023-08-31 13:51:22"
    },
    {
      "id": 5,
      "details": [
        {
          "id": 7,
          "product": {
            "id": 9,
            "name": "멀티 어댑터 5구",
            "reviewCount": 0,
            "createAt": "2023-08-31 01:24:49"
          },
          "productName": "멀티 어댑터 5구",
          "price": 8900,
          "quantity": 1,
          "state": "REQUESTED"
        }
      ],
      "totalAmount": 8900,
      "createAt": "2023-08-31 13:50:51"
    }
  ],
  "error": null
}
```
 > Response headers: x-pagination: {"totalCount":8,"pageSize":5,"currentPage":1,"totalPages":2,"hasNext":false,"hasPrevious":true}

---

#### 개별 주문 조회

인증된 사용자 본인의 개별 주문을 출력한다.

주문은 `Review`를 포함할 수 있다. (샘플 데이터에서 `id=9`인 주문은 리뷰를 포함한다.)

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: getOrder
- URL: GET /api/orders/{id}
  * {id}: 조회 대상 주문의 PK
- Response Body: 주문 내용
```json
{
  "success": true,
  "data": {
    "id": 9,
    "details": [
      {
        "id": 11,
        "product": {
          "id": 7,
          "name": "여행용 파우치",
          "reviewCount": 1,
          "createAt": "2023-08-08 22:37:29"
        },
        "review": {
          "id": 1,
          "productId": 7,
          "content": "잘 사용하겠습니다.",
          "createAt": "2023-08-31 15:04:16"
        },
        "productName": "여행용 파우치",
        "price": 17900,
        "quantity": 1,
        "state": "COMPLETED",
        "completedAt": "2023-08-31 15:03:32",
        "createAt": "2023-08-31 13:58:22"
      }
    ],
    "totalAmount": 17900,
    "createAt": "2023-08-31 13:58:22"
  },
  "error": null
}
```

#### 주문 배송중 처리

인증된 관리자가 주문에 대해 상태를 변경한다.

주문이 최초 생성될 때 주문 상태 `state`는 `REQUESTED`이다. 주문 상태가 `REQUESTED`라면 접수 처리를 할 수 있다. 정상적으로 접수 처리 되면 주문 상태는 `SHIPPING`로 변경된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 관리자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: shipping
- URL: PATCH /api/orders/{id}/orders-details/{orderDetailId}/shipping
  * {id}: 상태를 변경할 주문의 PK
  * {orderDetailsId}: 주문 상세정보 PK
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "data": true,
  "error": null
}
```
- 주문 상태 `state`가 `REQUESTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "data": false,
  "error": null
}
```

#### 주문 거절 처리

인증된 관리자가 주문에 대해 상태를 변경한다.

주문이 최초 생성될 때 주문 상태 `state`는 `REQUESTED`이다. 주문 상태가 `REQUESTED`라면 거절 처리를 할 수 있다. 정상적으로 거절 처리 되면 주문 상태는 `REJECTED`로 변경된다. 그리고 주문 거절 시각 `rejectedAt`은 현재 시각으로 설정된다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 관리자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: reject
- URL: PATCH /api/orders/{id}/orders-details/{orderDetailId}/reject
  * {id}: 상태를 변경할 주문의 PK
  * {orderDetailsId}: 주문 상세정보 PK

- Request Body: 거절 메세지
```json
{
  "message": "reject message"
}
```
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "data": true,
  "error": null
}
```
- 주문 상태 `state`가 `REQUESTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "data": false,
  "error": null
}
```

#### 주문 확정 처리

인증된 사용자 본인의 주문에 대해 상태를 변경한다.

주문 상태 `state`가 `SHIPPING`이라면 주문 확정 처리를 할 수 있다.

상태 변경이 불가능하다면 예외를 발생시키지 말고 `false`를 정상 반환한다.

- 구분: 인증 사용자용 API
- 구현 컨트롤러: com.orders.orders.OrderRestController
- 컨트롤러 메소드명: complete
- URL: PATCH /api/orders/{id}/orders-details/{orderDetailId}/complete
  * {id}: 상태를 변경할 주문의 PK
- Response Body: true 라면, 상태변경 성공
```json
{
  "success": true,
  "data": true,
  "error": null
}
```
- 주문 상태 `state`가 `ACCEPTED`가 아닌 경우 응답 예시
```json
{
  "success": true,
  "data": false,
  "error": null
}
```
