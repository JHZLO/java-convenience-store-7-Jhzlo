package store.controller;

import java.util.List;
import store.domain.Membership;
import store.domain.Price;
import store.domain.Receipt;
import store.domain.dto.PromotionResultDto;
import store.domain.order.OrderProduct;
import store.domain.order.Orders;
import store.domain.product.Products;
import store.domain.promotion.Promotions;
import store.util.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        Promotions promotions = new Promotions("promotions.md");
        Products products = new Products("products.md", promotions);
        products.addProductWithoutPromotion();

        boolean flag = true;
        while (flag) {
            try {
                useConvenienceStore(products, promotions);
                String userInput = inputView.readAdditionalPurchase();
                InputValidator.validateUserInput(userInput);
                flag = "Y".equals(userInput);
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }

    public void useConvenienceStore(Products products, Promotions promotions) {
        outputView.printHeaderNotice();
        outputView.printProductNotice();

        // 최신 재고 상태 출력
        outputView.printResult(products.getProductsAsString());

        Orders orders = inputOrderProduct(products);
        List<OrderProduct> orderProducts = orders.getOrderProducts();
        PromotionResultDto promotionResultDto = handlePromotion(orderProducts);

        Membership membership = handleMembership();
        Price price = new Price(promotionResultDto.orderProducts(), membership, promotionResultDto.discountQuantity());

        Receipt receipt = new Receipt(orderProducts, price, promotionResultDto.discountQuantity());
        System.out.println(receipt);
    }

    private Orders inputOrderProduct(Products products) {
        while (true) {
            try {
                String input = inputView.readOrderProduct();
                return new Orders(input, products);
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }

    private PromotionResultDto handlePromotion(List<OrderProduct> orderProducts) {
        int totalDiscountQuantity = 0;
        for (OrderProduct orderProduct : orderProducts) {
            while (true) {
                try {
                    if (orderProduct.isBenefitExceedsPromotionStock() && orderProduct.hasPromotionOnDate()) {
                        totalDiscountQuantity = orderProduct.calculateDiscountQuantity();
                        if (!handleInsufficientPromotionStock(orderProduct)) {
                            break;
                        }
                        orderProduct.buyProduct();
                        break;
                    }
                    totalDiscountQuantity = orderProduct.calculateDiscountQuantity();
                    orderProduct.buyProduct();
                    if (orderProduct.hasAcquireBenefitPromotion() && orderProduct.hasPromotionOnDate()) {
                        String userInput = inputView.readPromotionBenefit(orderProduct.getName(),
                                orderProduct.getBenefitCount());
                        InputValidator.validateUserInput(userInput); // 유효성 검사
                        if ("Y".equals(userInput)) {
                            totalDiscountQuantity = orderProduct.calculateDiscountQuantity();
                            orderProduct.applyPromotion(orderProduct.getBenefitCount());
                        }
                        if ("N".equals(userInput)) {
                            totalDiscountQuantity = 0;
                        }
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    outputView.printResult(e.getMessage());
                }
            }
        }
        return new PromotionResultDto(orderProducts, totalDiscountQuantity);
    }


    private boolean handleInsufficientPromotionStock(OrderProduct orderProduct) {
        while (true) {
            try {
                String userInput = inputView.readContinueBuy(orderProduct.getName(), orderProduct.getTotalBenefit());
                InputValidator.validateUserInput(userInput);

                if ("Y".equals(userInput)) {
                    return true;
                }
                if ("N".equals(userInput)) {
                    orderProduct.setResetQuantity();
                    return false;
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }

    private Membership handleMembership() {
        while (true) {
            try {
                String userInput = inputView.readUseMemberShip();
                InputValidator.validateUserInput(userInput);
                Membership membership;
                if ("Y".equals(userInput)) {
                    return new Membership(true);
                }
                if ("N".equals(userInput)) {
                    return new Membership(false);
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }
}
