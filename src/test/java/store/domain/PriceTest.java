package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.order.OrderProduct;
import store.domain.product.Product;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import store.domain.promotion.Promotion;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    private Membership membership;
    private List<Product> products;
    private OrderProduct orderProduct1;
    private OrderProduct orderProduct2;
    private Promotion promotion;

    @BeforeEach
    void 초기화() {
        promotion = new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01");
        Product product1 = new Product("Product1", 10000, 10, promotion);
        Product product2 = new Product("Product2", 5000, 5, null);

        products = Arrays.asList(product1, product2);

        orderProduct1 = new OrderProduct(Collections.singletonList(product1), 3);
        orderProduct2 = new OrderProduct(Collections.singletonList(product2), 2);

        membership = new Membership(true); // 멤버십 회원
    }

    @Test
    void 총가격_계산() {
        Price price = new Price(Arrays.asList(orderProduct1, orderProduct2), membership, 0);
        int totalPrice = price.calculateTotalPrice();

        assertEquals(40000, totalPrice); // (3 * 10000) + (2 * 5000)
    }

    @Test
    void 프로모션_할인_계산() {
        // 프로모션 설정 (2+1 행사)
        Product productWithPromotion = products.get(0);

        OrderProduct orderProductWithPromotion = new OrderProduct(Collections.singletonList(productWithPromotion), 6);

        Price price = new Price(Collections.singletonList(orderProductWithPromotion), membership, 2);
        int promotionDiscount = price.calculatePromotionDiscount();

        assertEquals(20000, promotionDiscount); // 2개의 가격 할인 (2+1 프로모션)
    }

    @Test
    void 멤버십_할인_계산_최대() {
        Price price = new Price(Arrays.asList(orderProduct1, orderProduct2), membership, 0);
        int membershipDiscount = price.calculateMembershipDiscount();

        assertEquals(8000, membershipDiscount); // 30% 할인 적용, 최대 할인 금액 8000원씩 제한
    }

    @Test
    void 멤버십_할인_계산_적정() {
        Price price = new Price(Arrays.asList(orderProduct2), membership, 0);
        int membershipDiscount = price.calculateMembershipDiscount();

        assertEquals(3000, membershipDiscount); // 30% 할인 적용, 10000 * 0.3
    }

    @Test
    void 최종가격_계산() {
        Price price = new Price(Arrays.asList(orderProduct1, orderProduct2), membership, 0);
        int finalPrice = price.calculateFinalPrice();

        assertEquals(32000, finalPrice); // 총 가격 40000 - 멤버십 할인 8000
    }

    @Test
    void 멤버십_없는_경우_최종가격_계산() {
        Membership nonMember = new Membership(false); // 비회원
        Price price = new Price(Arrays.asList(orderProduct1, orderProduct2), nonMember, 0);
        int finalPrice = price.calculateFinalPrice();

        assertEquals(40000, finalPrice); // 총 가격 40000 (할인 없음)
    }
}
