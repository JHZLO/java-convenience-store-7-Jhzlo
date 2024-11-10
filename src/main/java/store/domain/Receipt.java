package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.order.OrderProduct;

public class Receipt {
    private static final String PURCHASE_HEADER = "상품명\t\t수량\t금액\n";
    private static final String TOTAL_PURCHASE = "총구매액\t\t%,d\t%d\n";
    private static final String PROMOTION_DISCOUNT = "행사할인\t\t\t-%,d\n";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인\t\t-%,d\n";
    private static final String FINAL_PRICE = "내실돈\t\t\t%,d\n";

    private final List<String> purchaseDetails; // 구매 상품 내역
    private final List<String> giftDetails; // 증정 상품 내역
    private final Price price;

    public Receipt(List<OrderProduct> orderProducts, Price price, int discountQuantity) {
        this.purchaseDetails = parsePurchaseDetails(orderProducts);
        this.giftDetails = parseGiftDetails(orderProducts, discountQuantity);
        this.price = price;
    }

    // 구매 내역 생성
    private List<String> parsePurchaseDetails(List<OrderProduct> orderProducts) {
        List<String> purchase = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            int totalPrice = orderProduct.getQuantity() * orderProduct.getProduct().get(0).getPrice();
            purchase.add(String.format("%s\t\t%,d\t%,d",
                    orderProduct.getName(),
                    orderProduct.getQuantity(),
                    totalPrice
            ));
        }
        return purchase;
    }

    // 증정 내역 생성
    private List<String> parseGiftDetails(List<OrderProduct> orderProducts, int discountQuantity) {
        List<String> gift = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.hasPromotionOnDate()) {
                gift.add(String.format("%s\t\t%,d",
                        orderProduct.getName(),
                        discountQuantity
                ));
            }
        }
        return gift;
    }

    @Override
    public String toString() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==============W 편의점================\n");
        receipt.append(PURCHASE_HEADER);
        for (String detail : purchaseDetails) {
            receipt.append(detail).append("\n");
        }
        receipt.append("=============증\t정===============\n");
        for (String gift : giftDetails) {
            receipt.append(gift).append("\n");
        }
        receipt.append("====================================\n");
        receipt.append(String.format(TOTAL_PURCHASE, purchaseDetails.size(), price.calculateTotalPrice()));
        receipt.append(String.format(PROMOTION_DISCOUNT, price.calculatePromotionDiscount()));
        receipt.append(String.format(MEMBERSHIP_DISCOUNT, price.calculateMembershipDiscount()));
        receipt.append(String.format(FINAL_PRICE, price.calculateFinalPrice()));
        return receipt.toString();
    }
}
