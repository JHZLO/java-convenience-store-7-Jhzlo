package store.domain.product;

import java.time.LocalDateTime;
import store.domain.promotion.Promotion;

public class Product {
    private final String name;
    private final int price;
    private Integer quantity;
    private final Promotion promotion;

    public Product(String name, int price, Integer quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public void updateQuantity(int count) {
        quantity -= count;
    }

    public boolean isValidProductName(String inputName) {
        if (name.equals(inputName)) {
            return true;
        }
        return false;
    }

    public boolean hasPromotion() {
        if (promotion == null) {
            return false;
        }
        return true;
    }

    public boolean onDatePromotion(LocalDateTime orderDate) {
        return promotion.isApplicableOnDate(orderDate);
    }

    @Override
    public String toString() {
        String promotionText = "";
        if (promotion != null) {
            promotionText = promotion.getPromotionName();
        }
        String quantityText;
        if (quantity > 0) {
            quantityText = quantity + "개";
        } else {
            quantityText = "재고없음";
        }
        return String.format("- %s %d원 %s %s", name, price, quantityText, promotionText);
    }

    public int getPromotionBuyCount() {
        return promotion.getBuyCount();
    }

    public int getPromotionBenifitCount() {
        return promotion.getBenefitCount();
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
