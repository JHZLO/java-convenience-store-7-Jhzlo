package store.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static store.constants.ErrorMessage.ERROR_GENERIC_INVALID_INPUT;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

class OrderProductTest {
    private List<Product> productsWithPromotion;
    private List<Product> productsWithoutPromotion;

    @BeforeEach
    void 초기화() {
        Promotion promotion = new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01");
        Product productWithPromotion = new Product("사이다", 1000, 10, promotion);
        Product productWithoutPromotion = new Product("사이다", 1000, 5, null);

        productsWithPromotion = Arrays.asList(productWithPromotion);
        productsWithoutPromotion = Arrays.asList(productWithoutPromotion);
    }

    @Test
    void 주문_생성_유효성_검사_성공() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 5);
        assertEquals(5, orderProduct.getQuantity());
    }

    @Test
    void 주문_생성_재고_초과_예외발생() {
        assertThatThrownBy(() -> new OrderProduct(productsWithPromotion, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_QUANTITY_EXCEEDS_STOCK);
    }

    @Test
    void 주문_생성_재고_없음_예외발생() {
        assertThatThrownBy(() -> new OrderProduct(productsWithPromotion, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_GENERIC_INVALID_INPUT);
    }

    @Test
    void 프로모션_할인_적용가능한_수량_계산() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 7);
        int discountQuantity = orderProduct.calculateDiscountQuantity();
        assertEquals(2, discountQuantity); // 2+1 프로모션에서 7개 주문 시 2개 할인
    }

    @Test
    void 프로모션_할인_금액_계산() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 7);
        int discountQuantity = orderProduct.calculateDiscountQuantity();
        int discountAmount = orderProduct.calculatePromotionDiscount(discountQuantity);
        assertEquals(2000, discountAmount); // 할인된 상품 2개 × 1000원
    }

    @Test
    void 프로모션_혜택_적용() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 6);
        orderProduct.applyPromotion(1);
        assertEquals(7, orderProduct.getQuantity());
    }

    @Test
    void 프로모션이_없는_재고_처리() {
        OrderProduct orderProduct = new OrderProduct(productsWithoutPromotion, 3);
        assertFalse(orderProduct.hasAcquireBenefitPromotion());
        assertEquals(3, orderProduct.getQuantity());
    }

    @Test
    void 주문재고차감() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 5);
        orderProduct.buyProduct();
        assertEquals(5, productsWithPromotion.get(0).getQuantity());
    }

    @Test
    void 재고_초기화() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 5);
        orderProduct.setResetQuantity();
        assertEquals(0, orderProduct.getQuantity());
    }

    @Test
    void 총_혜택_수량_계산() {
        OrderProduct orderProduct = new OrderProduct(productsWithPromotion, 10);
        int totalBenefit = orderProduct.calculateDiscountQuantity();
        assertEquals(3, totalBenefit); // 2+1 프로모션에서 10개 주문 시 3개 혜택 계산
    }

    @Test
    void 프로모션이_없는_상품_주문시_할인불가능() {
        OrderProduct orderProduct = new OrderProduct(productsWithoutPromotion, 5);
        assertEquals(0, orderProduct.calculateDiscountQuantity());
        assertEquals(0, orderProduct.calculatePromotionDiscount(0));
    }
}
