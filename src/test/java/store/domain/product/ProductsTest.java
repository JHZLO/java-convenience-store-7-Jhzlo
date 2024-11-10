package store.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.constants.ErrorMessage.ERROR_NON_EXISTENT_PRODUCT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;

import java.util.List;

class ProductsTest {
    private Products products;

    @BeforeEach
    void 초기화() {
        Promotions promotions = new Promotions("promotions.md");
        products = new Products("products.md", promotions);
    }

    @Test
    void 상품명으로_재고_조회() {
        List<Product> matchingProducts = products.findProductsByName("콜라");
        assertThat(matchingProducts).isNotEmpty();
        assertThat(matchingProducts.get(0).getName()).isEqualTo("콜라");
    }

    @Test
    void 존재하지_않는_상품명으로_재고_조회_예외발생() {
        assertThatThrownBy(() -> products.findProductsByName("존재하지않는상품"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_NON_EXISTENT_PRODUCT);
    }

    @Test
    void 프로모션_없는_상품추가() {
        products.addProductWithoutPromotion();
        List<Product> matchingProducts = products.findProductsByName("오렌지주스");

        assertThat(matchingProducts).hasSize(2); // 프로모션이 없는 재고가 추가되어야 함
        assertThat(matchingProducts.get(1).getQuantity()).isEqualTo(0); // 추가된 상품의 재고는 0
    }

    @Test
    void 상품목록_문자열_출력() {
        String productsAsString = products.getProductsAsString();
        assertThat(productsAsString).contains("- 콜라 1,000원");
        assertThat(productsAsString).contains("- 오렌지주스 1,800원");
    }

    @Test
    void 상품명으로_프로모션_조회() {
        Promotion promotion = products.getPromotionByName("콜라");
        assertThat(promotion).isNotNull();
        assertThat(promotion.getPromotionName()).isEqualTo("탄산2+1");
    }

    @Test
    void 프로모션_없는_상품_조회시_결과없음() {
        Promotion promotion = products.getPromotionByName("물");
        assertThat(promotion).isNull();
    }
}
