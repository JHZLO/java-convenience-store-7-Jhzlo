package store.domain;

import java.util.List;
import store.domain.order.OrderProduct;

public class Price {
    private final List<OrderProduct> orderProducts;
    private final Membership membership;
    private final int discountQuantity;

    public Price(List<OrderProduct> orderProducts, Membership membership, int discountQuantity) {
        this.orderProducts = orderProducts;
        this.membership = membership;
        this.discountQuantity = discountQuantity;
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getQuantity() * orderProduct.getProduct().get(0).getPrice();
        }
        return totalPrice;
    }

    public int calculatePromotionDiscount() {
        int totalDiscount = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalDiscount += orderProduct.calculatePromotionDiscount(discountQuantity);
        }
        return totalDiscount;
    }

    public int calculateMembershipDiscount() {
        int totalDiscount;
        int remainingAmount = 0;
        for (OrderProduct orderProduct : orderProducts) {
            int totalPrice = orderProduct.getQuantity() * orderProduct.getProduct().get(0).getPrice();
            int promotionDiscount = orderProduct.calculatePromotionDiscount(discountQuantity);
            remainingAmount += totalPrice - promotionDiscount;
        }
        totalDiscount = membership.calculateDiscount(remainingAmount);
        return totalDiscount;
    }

    public int calculateFinalPrice() {
        int totalPrice = calculateTotalPrice();
        int promotionDiscount = calculatePromotionDiscount();
        int membershipDiscount = calculateMembershipDiscount();
        return totalPrice - promotionDiscount - membershipDiscount;
    }
}
