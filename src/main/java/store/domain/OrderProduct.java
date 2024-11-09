package store.domain;

import static store.constants.ErrorMessage.ERROR_GENERIC_INVALID_INPUT;
import static store.constants.ErrorMessage.ERROR_NON_EXISTENT_PRODUCT;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;

public class OrderProduct {
    private static final Integer MIN_QUANTITY = 1;
    private final List<Product> products; // 행사 중인 상품과 행사하지 않는 상품으로 나뉠 수 있으니까
    private final int quantity;
    private final LocalDateTime orderDate;

    public OrderProduct(List<Product> products, int quantity) {
        validateOrder(products, quantity);
        this.products = products;
        this.quantity = quantity;
        this.orderDate = DateTimes.now();
        buyProduct(products,quantity);
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

    public void buyProduct(List<Product> matchingProducts, int remainingQuantity) {
        for (Product product : matchingProducts) {
            int productStock = product.getQuantity();
            int deduction = Math.min(remainingQuantity, productStock); // 차감할 수 있는 최대 수량
            product.updateQuantity(deduction); // 재고 차감
            remainingQuantity -= deduction;

            if (remainingQuantity == 0) {
                break;
            }
        }
    }

    public boolean hasPromotionOnDate() {
        for(Product product : products){
            if(product.hasPromotion()){
                return product.onDatePromotion(orderDate);
            }
        }
        return false;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Product> getProduct() {
        return products;
    }
}
