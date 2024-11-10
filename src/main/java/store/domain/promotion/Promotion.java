package store.domain.promotion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Promotion {
    private final String name;
    private final int buyCount;
    private final int benefitCount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Promotion(String name, int buyCount, int benefitCount, String startDate, String endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.benefitCount = benefitCount;
        this.startDate = LocalDateTime.parse(startDate + " 00:00", DATE_FORMATTER);
        this.endDate = LocalDateTime.parse(endDate + " 23:59", DATE_FORMATTER);
    }

    public boolean isMatchPromotion(String promotionName) {
        if (name.equals(promotionName)) {
            return true;
        }
        return false;
    }

    public boolean isApplicableOnDate(LocalDateTime orderDate) {
        return (orderDate.isEqual(startDate) || orderDate.isAfter(startDate))
                && (orderDate.isEqual(endDate) || orderDate.isBefore(endDate));
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getBenefitCount() {
        return benefitCount;
    }

    public String getPromotionName() {
        return name;
    }
}
