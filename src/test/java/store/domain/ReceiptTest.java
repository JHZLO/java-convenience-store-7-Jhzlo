package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.order.OrderProduct;
import store.domain.product.Product;

import java.util.Collections;
import java.util.List;
import store.domain.promotion.Promotion;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private List<OrderProduct> orderProducts;
    private Price price;
    private Promotion promotion;

    @BeforeEach
    void 초기설정() {
        promotion = new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01");

        Product product1 = new Product("Product1", 4000, 10, promotion);
        Product product2 = new Product("Product2", 5000, 5, null);

        OrderProduct orderProduct1 = new OrderProduct(Collections.singletonList(product1), 3);
        OrderProduct orderProduct2 = new OrderProduct(Collections.singletonList(product2), 2);

        orderProducts = List.of(orderProduct1, orderProduct2);

        Membership membership = new Membership(true); // 멤버십 회원
        price = new Price(orderProducts, membership, 0);
    }

    @Test
    void 영수증_생성_및_구매내역_확인() {
        Receipt receipt = new Receipt(orderProducts, price, 0);

        assertNotNull(receipt);
        assertTrue(receipt.toString().contains("Product1"));
        assertTrue(receipt.toString().contains("Product2"));
        assertTrue(receipt.toString().contains("총구매액"));
    }

    @Test
    void 구매내역_포함여부_확인() {
        Receipt receipt = new Receipt(orderProducts, price, 0);

        String receiptString = receipt.toString();

        assertTrue(receiptString.contains("Product1"));
        assertTrue(receiptString.contains("Product2"));
        assertTrue(receiptString.contains("3")); // Product1의 수량
        assertTrue(receiptString.contains("2")); // Product2의 수량
    }

    @Test
    void 총구매금액_및_할인금액_계산() {
        Receipt receipt = new Receipt(orderProducts, price, 0);

        String receiptString = receipt.toString();
        
        // 행사 할인 금액 확인
        assertTrue(receiptString.contains(String.format("행사할인\t\t\t-%,d", price.calculatePromotionDiscount())));
        // 멤버십 할인 금액 확인
        assertTrue(receiptString.contains(String.format("멤버십할인\t\t-%,d", price.calculateMembershipDiscount())));
        // 최종 결제 금액 확인
        assertTrue(receiptString.contains(String.format("내실돈\t\t\t%,d", price.calculateFinalPrice())));
    }

    @Test
    void 증정상품_내역_확인() {
        Product productWithPromotion = new Product("PromotionProduct", 5000, 10, promotion);
        OrderProduct orderProductWithPromotion = new OrderProduct(Collections.singletonList(productWithPromotion), 6);

        List<OrderProduct> orderProductsWithPromotion = List.of(orderProductWithPromotion);

        Membership membership = new Membership(false);
        Price priceWithPromotion = new Price(orderProductsWithPromotion, membership, 2);

        Receipt receipt = new Receipt(orderProductsWithPromotion, priceWithPromotion, 2);

        String receiptString = receipt.toString();

        assertTrue(receiptString.contains("PromotionProduct"));
        assertTrue(receiptString.contains("3")); // 증정 수량
    }
}
