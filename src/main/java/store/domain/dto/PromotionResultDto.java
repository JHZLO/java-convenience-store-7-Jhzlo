package store.domain.dto;

import java.util.List;
import store.domain.order.OrderProduct;

public record PromotionResultDto(List<OrderProduct> orderProducts, int discountQuantity) {
}
