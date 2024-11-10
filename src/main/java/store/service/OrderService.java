package store.service;

import store.domain.order.Orders;
import store.domain.product.Products;
import store.util.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class OrderService {
    public Orders inputOrder(InputView inputView, OutputView outputView, Products products) {
        while (true) {
            try {
                String input = inputView.readOrderProduct();
                return new Orders(input, products);
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }

    public boolean checkAdditionalPurchase(InputView inputView, OutputView outputView) {
        String userInput = inputView.readAdditionalPurchase();
        InputValidator.validateUserInput(userInput);
        return "Y".equals(userInput);
    }
}
