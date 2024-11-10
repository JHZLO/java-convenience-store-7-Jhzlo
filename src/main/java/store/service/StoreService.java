package store.service;

import store.domain.Membership;
import store.domain.Price;
import store.domain.Receipt;
import store.domain.dto.PromotionResultDto;
import store.domain.order.Orders;
import store.domain.product.Products;
import store.domain.promotion.Promotions;
import store.view.InputView;
import store.view.OutputView;

public class StoreService {
    private static final String PROMOTIONS_FILE_NAME = "promotions.md";
    private static final String PRODUCTS_FILE_NAME = "products.md";

    private Promotions promotions;
    private Products products;
    private final PromotionService promotionService;
    private final OrderService orderService;
    private final MembershipService membershipService;

    public StoreService() {
        this.promotions = new Promotions(PROMOTIONS_FILE_NAME);
        this.products = new Products(PRODUCTS_FILE_NAME, promotions);
        this.products.addProductWithoutPromotion();

        this.promotionService = new PromotionService();
        this.orderService = new OrderService();
        this.membershipService = new MembershipService();
    }

    public String getProductList() {
        return products.getProductsAsString();
    }

    public Receipt processStore(InputView inputView, OutputView outputView) {
        Orders orders = orderService.inputOrder(inputView, outputView, products);
        PromotionResultDto promotionResultDto = promotionService.applyPromotions(orders.getOrderProducts(), inputView,
                outputView);

        Membership membership = membershipService.handleMembership(inputView, outputView);
        Price price = new Price(promotionResultDto.orderProducts(), membership, promotionResultDto.discountQuantity());

        return new Receipt(orders.getOrderProducts(), price, promotionResultDto.discountQuantity());
    }
}
