package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.order.OrderProduct;
import store.domain.order.Price;

public class Receipt {
    private final List<String> purchaseDetails; // 구매 상품 내역
    private final List<String> giftDetails; // 증정 상품 내역
    private final Price price;

    public Receipt(List<OrderProduct> orderProducts, Price price) {
        this.purchaseDetails = parsePurchaseDetails(orderProducts);
        this.giftDetails = parseGiftDetails(orderProducts);
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
    private List<String> parseGiftDetails(List<OrderProduct> orderProducts) {
        List<String> gift = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.hasPromotionOnDate()) {
                gift.add(String.format("%s\t\t%,d",
                        orderProduct.getName(),
                        orderProduct.calculateDiscountQuantity()
                ));
            }
        }
        return gift;
    }

    @Override
    public String toString() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==============W 편의점================\n");
        receipt.append("상품명\t\t수량\t금액\n");
        for (String detail : purchaseDetails) {
            receipt.append(detail).append("\n");
        }
        receipt.append("=============증\t정===============\n");
        for (String gift : giftDetails) {
            receipt.append(gift).append("\n");
        }
        receipt.append("====================================\n");
        receipt.append(String.format("총구매액\t\t%,d\t%d\n", purchaseDetails.size(), price.calculateTotalPrice()));
        receipt.append(String.format("행사할인\t\t\t-%,d\n", price.calculatePromotionDiscount()));
        receipt.append(String.format("멤버십할인\t\t-%,d\n", price.calculateMembershipDiscount()));
        receipt.append(String.format("내실돈\t\t\t%,d\n", price.calculateFinalPrice()));
        return receipt.toString();
    }
}
