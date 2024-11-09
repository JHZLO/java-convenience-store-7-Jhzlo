package store.domain.order;

import static store.constants.ErrorMessage.ERROR_INVALID_FORMAT;

import java.util.ArrayList;
import java.util.List;
import store.domain.product.Products;
import store.util.InputParser;

public class Orders {
    private static final String VALID_INPUT_PATTERN = "^\\[[가-힣]+-(\\d+)](,\\[[가-힣]+-(\\d+)])*$";
    private static final String SQUARE_BRACKETS = "[\\[\\]]";
    private static final int MENU_LENGTH = 2;
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_QUANTITY_INDEX = 1;

    private final List<OrderProduct> orderProducts;

    public Orders(String inputOrders, Products products) {
        validateOrdersFormat(inputOrders);
        this.orderProducts = addOrders(inputOrders, products);
    }

    private void validateOrdersFormat(String input) {
        if (input.matches(VALID_INPUT_PATTERN)) {
            return;
        }
        throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
    }

    private List<OrderProduct> addOrders(String input, Products products) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        String[] parts = input.split(",");
        for (String part : parts) {
            String[] menu = part.replaceAll(SQUARE_BRACKETS, "").split("-");
            if (menu.length == MENU_LENGTH) {
                String name = menu[PRODUCT_NAME_INDEX];
                int quantity = InputParser.parseInt(menu[PRODUCT_QUANTITY_INDEX]);
                orderProducts.add(new OrderProduct(products.findProductsByName(name), quantity));
            }
        }
        return orderProducts;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
