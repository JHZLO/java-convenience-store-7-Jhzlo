package store.domain.promotion;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PromotionTest {
    @Test
    void 특정_날짜에_프로모션이_적용되는지_확인() {
        Promotion promotion = new Promotion("할인 프로모션", 3, 1, "2024-11-01", "2024-11-30");
        LocalDateTime applicableDate = LocalDateTime.of(2024, 11, 15, 12, 0);
        LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 30, 23, 59);
        LocalDateTime nonApplicableDate = LocalDateTime.of(2024, 12, 1, 0, 0);

        assertTrue(promotion.isApplicableOnDate(applicableDate));
        assertTrue(promotion.isApplicableOnDate(startDate));
        assertTrue(promotion.isApplicableOnDate(endDate));
        assertFalse(promotion.isApplicableOnDate(nonApplicableDate));
    }
}
