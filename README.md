# 🏬편의점 미션
## 💡기능 목록 구현하기
> 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.
---

### 1️⃣ 편의점 안내문 출력
- [x] 편의점 안내문을 출력한다
```
"안녕하세요. W편의점입니다."
```

---

### 2️⃣ 보유하고 있는 상품 출력
- [x] 보유 상품 안내문을 출력한다.
```
현재 보유하고 있는 상품입니다.
```

- [x] 편의점 재고를 출력한다.
    - [x] 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
      `src/main/resources/products.md`과 `src/main/resources/promotions.md` 파일을 이용한다.
    - [ ] 두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.


```text
- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개
```
---

### 3️⃣ 구매할 상품과 수량을 입력
- [x] 구매할 상품과 수량 안내문을 출력한다.
```
"구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
```
- [x] 구매할 상품과 수량을 입력받는다.
    -  상품명, 수량은 `하이픈(-)`으로, 개별 상품은 `대괄호([])`로 묶어 `쉼표(,)`로 구분한다.
```text
입력 값 예시 : [콜라-10],[사이다-3]
```
- [ ] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- [ ] 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
- [x] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.

- [x] 📢 예외 처리
    - `(-)`,`([])`,`(,)` 이외의 특수 문자가 있는 경우
        - 예외 메시지: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - 편의점 재고보다 더 많은 재고를 입력하게 된 경우
        - 예외 메시지: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요`.
    - 0 이하의 재고가 들어온 경우
        - 예외 메시지: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`
    - 메뉴에 없는 메뉴명이 입력된 경우
        - 예외 메시지: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
    - 중복된 메뉴가 있는 경우도?

---

### 4️⃣ 프로모션 적용 여부
- [ ] 프로모션
    - [ ] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
    - [ ] 프로모션은 N개 구매 시 1개 무료 증정`(Buy N Get 1 Free)`의 형태로 진행된다.
        - buyCount가 1인 프로모션 -> quantity가 홀수인 경우 -> 1 Free 받을지 물어봄
        - buyCount가 2인 프로모션 -> quantity가 짝수인 경우 -> 1 Free 받을지 물어봄
            - 2개 샀을 때 -> 1 Free 물어봄
            - 5개 샀을 때 -> 1 Free 물어봄
            - 8개 삿을 때 -> 1 Free 물어봄
                - `3의배수 - 1` 인 경우에 물어봄

    - [ ] +1 또는 2+1 프로모션이 각각 `지정된 상품`에 적용되며, _동일 상품에 여러 프로모션이 적용되지 않는다_.
    - [ ] 프로모션 혜택은 `프로모션 재고 내`에서만 적용할 수 있다.
    - [ ] 프로모션 기간 중이라면 프로모션 재고를 `우선적으로 차감`하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.

- [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력
```text
현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
```
- [ ] 프로모션 적용 여부 입력 받기
    - `Y`: 증정 받을 수 있는 상품을 추가한다.
    - `N`: 증정 받을 수 있는 상품을 추가하지 않는다.

- [ ] 📢 예외처리
    - 예외 메시지: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - Y/N 이외의 값이 들어오게 된 경우

---

- [ ] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력
```text
현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
```
- [ ] 구매 여부 입력 받기
    - `Y`: 일부 수량에 대해 정가로 결제한다.
    - `N`: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
- [ ] 📢 예외처리
    - 예외 메시지: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - Y/N 이외의 값이 들어오게 된 경우

---

### 5️⃣ 멤버십 할인
- [ ] 멤버십 할인
    - [ ] 멤버십 회원은 프로모션 미적용 금액의 `30%`를 할인받는다.
    - [ ] 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
    - [ ] 멤버십 할인의 최대 한도는 `8,000원`이다.

- [ ] 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
```
멤버십 할인을 받으시겠습니까? (Y/N)
```

- [ ] 멤버십 할인의 유무를 입력 받는다.
    - `Y`: 멤버십 할인을 적용한다.
    - `N`: 멤버십 할인을 적용하지 않는다.

- [ ] 📢 예외처리
    - 예외 메시지: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - Y/N 이외의 값이 들어오게 된 경우
---
### 6️⃣ 영수증 출력
-[ ] 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
```text
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 	5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인		-3,000
내실돈			 9,000
```
영수증 항목은 아래와 같다.
- `구매 상품 내역`: 구매한 상품명, 수량, 가격
- `증정 상품 내역`: 프로모션에 따라 무료로 제공된 증정 상품의 목록
- 금액 정보
    - `총구매액`: 구매한 상품의 총 수량과 총 금액
    - `행사할인`: 프로모션에 의해 할인된 금액
    - `멤버십할인`: 멤버십에 의해 추가로 할인된 금액
    - `내실돈`: 최종 결제 금액

---
### 7️⃣ 추가 구매 여부 확인
- [ ] 추가 구매 여부를 확인하기 위한 안내 문구 출력
```text
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
```
- [ ] 추가 구매 여부 입력 받기
    - `Y`: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
    - `N`: 구매를 종료한다.
- [ ] 📢 예외처리
    - 예외 메시지: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - Y/N 이외의 값이 들어오게 된 경우

---

## 🎯 프로그래밍 요구 사항

- [ ] indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
- [ ] 3항 연산자를 쓰지 않는다.
- [ ] 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- [ ] JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인한
- [ ] else 예약어를 쓰지 않는다.
- [ ] Java Enum을 적용하여 프로그램을 구현한다.
- [ ] 구현한 기능에 대한 단위 테스트를 작성한다. 단, UI(System.out, System.in, Scanner) 로직은 제외한다
- [ ] 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- [ ] 입출력을 담당하는 클래스를 별도로 구현한다.

---

## 💻 라이브러리
- [ ] `camp.nextstep.edu.missionutils`에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
    - 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 `now()`를 활용한다.
    - 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 `readLine()`을 활용한다.
