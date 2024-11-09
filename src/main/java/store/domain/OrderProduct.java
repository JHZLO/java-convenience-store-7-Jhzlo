package store.domain;

import static store.constants.ErrorMessage.ERROR_GENERIC_INVALID_INPUT;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;

public class OrderProduct {
    private static final Integer MIN_QUANTITY = 1;
    private final List<Product> products; // 행사 중인 상품과 행사하지 않는 상품으로 나뉠 수 있으니까
    private final Integer quantity;
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
            throw new IllegalArgumentException(ERROR_GENERIC_INVALID_INPUT);
        }
    }

    private void validateTotalStock(List<Product> matchingProducts, int quantity) {
        int totalStock = matchingProducts.stream().mapToInt(Product::getQuantity).sum();

        if (quantity > totalStock) {
            throw new IllegalArgumentException(ERROR_QUANTITY_EXCEEDS_STOCK);
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

    public Integer getBenefitCount() {
        Product product = getProductByPromotion();
        if (hasBenefitPromotion() && hasPromotionOnDate()) {
            Promotion promotion = product.getPromotion();
            return promotion.getBenefitCount();
        }
        return null;
    }

    public boolean hasBenefitPromotion() {
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

    public void applyPromotion(int benefitCount){
        for(Product product : products){
            if(product.hasPromotion() && product.getQuantity() >= benefitCount){
                product.updateQuantity(benefitCount);
            }
            // TODO: 수량 적을시엔 물어볼 거임
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Product> getProduct() {
        return products;
    }

    public String getName(){
        String productName = null;
        for(Product product : products){
            productName =  product.getName();
        }
        return productName;
    }
}
