package store.domain;

import static store.constants.ErrorMessage.ERROR_GENERIC_INVALID_INPUT;
import static store.constants.ErrorMessage.ERROR_NON_EXISTENT_PRODUCT;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class OrderProduct {
    private static final Integer MIN_QUANTITY = 1;
    private final Product product;
    private final int quantity;
    private final LocalDateTime orderDate;

    public OrderProduct(Product product, int quantity) {
        validateOrder(product, quantity);
        this.product = product;
        this.quantity = quantity;
        this.orderDate = DateTimes.now();
    }

    private void validateOrder(Product product, int quantity) {
        validateQuantity(quantity);
        validateMatchName(product);
        validateExceedQuantity(quantity, product);
    }

    private void validateQuantity(int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException(ERROR_GENERIC_INVALID_INPUT);
        }
    }

    private void validateMatchName(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(ERROR_NON_EXISTENT_PRODUCT);
        }
    }

    private void validateExceedQuantity(int quantity, Product product) {
        if (isExceededQuantity(quantity, product)) {
            throw new IllegalArgumentException(ERROR_QUANTITY_EXCEEDS_STOCK);
        }
    }

    private boolean isExceededQuantity(int quantity, Product product) {
        return product.getQuantity() < quantity;
    }

    public void buyProduct() {
        product.updateQuantity(quantity);
    }

    public boolean hasPromotionOnDate() {
        return product.hasPromotion() || product.onDatePromotion(orderDate);
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
