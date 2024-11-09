package store.domain.order;

import static store.constants.ErrorMessage.ERROR_INVALID_FORMAT;

import java.util.ArrayList;
import java.util.List;
import store.domain.Membership;
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

    public int applyMembershipDiscount(Membership membership) {
        int totalFinalAmount = 0;

        for (OrderProduct orderProduct : orderProducts) {
            int totalPrice = orderProduct.getQuantity() * orderProduct.getProduct().get(0).getPrice(); // 총 금액
            int promotionDiscount = orderProduct.calculatePromotionDiscount(); // 프로모션 할인
            int remainingAmount = totalPrice - promotionDiscount; // 남은 금액
            int membershipDiscount = membership.calculateDiscount(remainingAmount); // 멤버십 할인 계산
            totalFinalAmount += (remainingAmount - membershipDiscount); // 최종 결제 금액 누적
        }

        return totalFinalAmount;
    }

    public List<OrderProduct> getOrderProducts(){
        return orderProducts;
    }
}