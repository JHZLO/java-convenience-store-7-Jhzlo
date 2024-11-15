package store.domain.order;

import static store.constants.ErrorMessage.GENERIC_INVALID_INPUT;
import static store.constants.ErrorMessage.QUANTITY_EXCEEDS_STOCK;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class OrderProduct {
    private static final Integer MIN_QUANTITY = 1;
    private final List<Product> products; // 행사 중인 상품과 행사하지 않는 상품으로 나뉠 수 있으니까
    private Integer quantity;
    private final LocalDateTime orderDate;

    public OrderProduct(List<Product> products, int quantity) {
        validateOrder(products, quantity);
        this.products = products;
        this.quantity = quantity;
        this.orderDate = DateTimes.now();
    }

    private void validateOrder(List<Product> products, int quantity) {
        validateQuantity(quantity);
        validateTotalStock(products, quantity);
    }

    private void validateQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(GENERIC_INVALID_INPUT.getMessage());
        }
    }

    private void validateTotalStock(List<Product> matchingProducts, int quantity) {
        int totalStock = matchingProducts.stream().mapToInt(Product::getQuantity).sum();

        if (quantity > totalStock) {
            throw new IllegalArgumentException(QUANTITY_EXCEEDS_STOCK.getMessage());
        }
    }

    public void buyProduct() {
        int totalQuantity = quantity;
        for (Product product : products) {
            int productStock = product.getQuantity();
            int deduction = Math.min(totalQuantity, productStock); // 차감할 수 있는 최대 수량
            product.updateQuantity(deduction); // 재고 차감
            totalQuantity -= deduction;
            if (totalQuantity == 0) {
                break;
            }
        }
    }

    public int getBenefitCount() {
        Product product = getProductByPromotion();
        if (hasAcquireBenefitPromotion() && hasPromotionOnDate()) {
            Promotion promotion = product.getPromotion();
            return promotion.getBenefitCount();
        }
        return 0;
    }

    public boolean hasAcquireBenefitPromotion() {
        Product product = getProductByPromotion();
        if (product != null) {
            Promotion promotion = product.getPromotion();
            int buyCount = promotion.getBuyCount();
            if (checkBenefitCondition(quantity, buyCount)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBenefitCondition(int orderQuantity, int buyCount) {
        return (buyCount == 1 && orderQuantity % 2 != 0) || (buyCount == 2 && orderQuantity % 3 == 2);
    }

    private Product getProductByPromotion() {
        for (Product product : products) {
            if (product.hasPromotion()) {
                return product;
            }
        }
        return null;
    }


    public boolean hasPromotionOnDate() {
        for (Product product : products) {
            if (product.hasPromotion()) {
                return product.onDatePromotion(orderDate);
            }
        }
        return false;
    }

    public boolean isBenefitExceedsPromotionStock() {
        int promotionStock = products.stream()
                .filter(Product::hasPromotion) // 프로모션이 있는 상품만 필터링
                .mapToInt(Product::getQuantity) // 해당 상품의 재고 수량 추출
                .sum(); // 재고 합산

        return quantity >= promotionStock; // 요청된 혜택 개수가 프로모션 재고보다 크면 true
    }

    public int getTotalBenefit() {
        int remainingPromotion = 0;
        int generalProduct = 0;
        for (Product product : products) {
            if (product.hasPromotion()) {
                remainingPromotion += product.getQuantity() % (product.getPromotionBenifitCount() + product.getPromotionBuyCount()); // 2+1 상품이면 3으로 나눴을 때 나머지
                generalProduct = quantity - product.getQuantity();
            }
        }
        return remainingPromotion + generalProduct;
    }

    public void applyPromotion(int benefitCount) {
        for (Product product : products) {
            if (product.hasPromotion() && product.getQuantity() >= quantity + benefitCount) {
                product.updateQuantity(benefitCount);
                this.quantity += benefitCount;
            }
        }
    }

    public int calculateDiscountQuantity() {
        int totalDiscountQuantity = 0;

        for (Product product : products) {
            if (product.hasPromotion()) {
                int buyCount = product.getPromotionBuyCount();
                int benefitCount = product.getPromotionBenifitCount();

                int applicableStock = Math.min(product.getQuantity(), quantity);
                int applicablePromotionSets = applicableStock / (buyCount + benefitCount); // 프로모션 세트 수 계산
                totalDiscountQuantity += applicablePromotionSets * benefitCount; // 할인받은 상품 개수
            }
        }

        return totalDiscountQuantity;
    }

    public int calculatePromotionDiscount(int discountQuantity) {
        int totalDiscount = 0;

        for (Product product : products) {
            if (product.hasPromotion() && hasPromotionOnDate()) {
                totalDiscount += discountQuantity * product.getPrice(); // 총 할인 금액 계산
            }
        }

        return totalDiscount;
    }

    public void setResetQuantity() {
        this.quantity = 0;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Product> getProduct() {
        return products;
    }

    public String getName() {
        String productName = null;
        for (Product product : products) {
            productName = product.getName();
        }
        return productName;
    }
}
