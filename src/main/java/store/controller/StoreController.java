package store.controller;

import java.util.List;
import store.domain.OrderProduct;
import store.domain.Orders;
import store.domain.Products;
import store.util.InputValidator;
import store.view.InputView;
import store.view.OutputView;
import store.domain.Promotions;

public class StoreController {
    private InputView inputView;
    private OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        outputView.printHeaderNotice();
        outputView.printProductNotice();

        Promotions promotions = new Promotions("promotions.md");
        Products products = new Products("products.md", promotions);
        outputView.printResult(products.getProductsAsString());

        Orders orders = inputOrderProduct(products);
        List<OrderProduct> orderProducts = orders.getOrderProducts();
        handlePromotion(orderProducts);

        outputView.printResult(products.getProductsAsString());
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

    private void handlePromotion(List<OrderProduct> orderProducts){
        for(OrderProduct orderProduct : orderProducts){
            while (true) {
                try {
                    if(orderProduct.isBenefitExceedsPromotionStock()){
                        if(!handleInsufficientPromotionStock(orderProduct)){
                            break;
                        };
                    }
                    orderProduct.buyProduct();
                    if (orderProduct.hasBenefitPromotion() && orderProduct.hasPromotionOnDate()) {
                        String userInput = inputView.readPromotionBenefit(orderProduct.getName(),
                                orderProduct.getBenefitCount());
                        InputValidator.validateUserInput(userInput); // 유효성 검사
                        if ("Y".equals(userInput)) {
                            orderProduct.applyPromotion(orderProduct.getBenefitCount());
                        }
                        if ("N".equals(userInput)) {
                        }
                    }
                    break;
                }catch (IllegalArgumentException e){
                    outputView.printResult(e.getMessage());
                }
            }
        }
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
                    return false;
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }
}
