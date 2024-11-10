package store.domain.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;

class ProductTest {
    private Product productWithPromotion;
    private Product productWithoutPromotion;
    private Promotion promotion;

    @BeforeEach
    void 초기화() {
        promotion = new Promotion("탄산2+1", 2, 1, "2024-11-01", "2024-12-01");
        productWithPromotion = new Product("콜라", 1000, 10, promotion);
        productWithoutPromotion = new Product("콜라", 1000, 5, null);
    }

    @Test
    void 재고차감_정상작동() {
        productWithPromotion.updateQuantity(3);
        assertThat(productWithPromotion.getQuantity()).isEqualTo(7);
    }

    @Test
    void 재고차감_초과수량_예외발생() {
        assertThatThrownBy(() -> productWithPromotion.updateQuantity(15))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_QUANTITY_EXCEEDS_STOCK);
    }

    @Test
    void 프로모션여부_확인_존재하는경우() {
        boolean hasPromotion = productWithPromotion.hasPromotion();
        assertThat(hasPromotion).isTrue();
    }

    @Test
    void 프로모션여부_확인_존재하지않는경우() {
        boolean hasPromotion = productWithoutPromotion.hasPromotion();
        assertThat(hasPromotion).isFalse();
    }

    @Test
    void 프로모션기간내_적용확인() {
        LocalDateTime orderDate = LocalDateTime.of(2024, 11, 15, 10, 0);
        boolean isApplicable = productWithPromotion.onDatePromotion(orderDate);
        assertThat(isApplicable).isTrue();
    }

    @Test
    void 프로모션기간외_적용확인() {
        LocalDateTime orderDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        boolean isApplicable = productWithPromotion.onDatePromotion(orderDate);
        assertThat(isApplicable).isFalse();
    }
}
