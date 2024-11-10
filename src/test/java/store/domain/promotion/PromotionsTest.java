package store.domain.promotion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromotionsTest {

    private static final String PROMOTION_FILE_NAME = "promotions.md";
    private Promotions promotions = new Promotions(PROMOTION_FILE_NAME);;

    @Test
    void 특정_프로모션_이름으로_프로모션을_찾는지_확인() {
        Promotion foundPromotion = promotions.findPromotionByName("MD추천상품");
        assertNotNull(foundPromotion);
        assertEquals("MD추천상품", foundPromotion.getPromotionName());

        Promotion notFoundPromotion = promotions.findPromotionByName("없는프로모션");
        assertNull(notFoundPromotion);
    }
}
