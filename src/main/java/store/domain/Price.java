package store.domain.order;

import java.util.List;
import store.domain.Membership;

public class Price {
    private final List<OrderProduct> orderProducts;
    private final Membership membership;

    public Price(List<OrderProduct> orderProducts, Membership membership) {
        this.orderProducts = orderProducts;
        this.membership = membership;
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
            totalDiscount += orderProduct.calculatePromotionDiscount();
        }
        return totalDiscount;
    }

    public int calculateMembershipDiscount() {
        int totalDiscount = 0;
        for (OrderProduct orderProduct : orderProducts) {
            int totalPrice = orderProduct.getQuantity() * orderProduct.getProduct().get(0).getPrice();
            int promotionDiscount = orderProduct.calculatePromotionDiscount();
            int remainingAmount = totalPrice - promotionDiscount;
            totalDiscount += membership.calculateDiscount(remainingAmount);
        }
        return totalDiscount;
    }

    public int calculateFinalPrice() {
        int totalPrice = calculateTotalPrice();
        int promotionDiscount = calculatePromotionDiscount();
        int membershipDiscount = calculateMembershipDiscount();
        return totalPrice - promotionDiscount - membershipDiscount;
    }
}
