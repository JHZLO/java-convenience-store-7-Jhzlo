package store.service;

import java.util.List;
import store.domain.dto.PromotionResultDto;
import store.domain.order.OrderProduct;
import store.util.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class PromotionService {
    public PromotionResultDto applyPromotions(List<OrderProduct> orderProducts, InputView inputView, OutputView outputView) {
        int totalDiscountQuantity = 0;

        for (OrderProduct orderProduct : orderProducts) {
            totalDiscountQuantity += processOrderProductPromotion(orderProduct, inputView, outputView);
        }

        return new PromotionResultDto(orderProducts, totalDiscountQuantity);
    }

    private int processOrderProductPromotion(OrderProduct orderProduct, InputView inputView, OutputView outputView) {
        int discountQuantity = 0;

        while (true) {
            try {
                if (orderProduct.isBenefitExceedsPromotionStock() && orderProduct.hasPromotionOnDate()) {
                    discountQuantity = handleExceedingPromotionStock(orderProduct, inputView, outputView);
                    if (discountQuantity == 0) break;
                } else {
                    discountQuantity = processPromotion(orderProduct, inputView, outputView);
                    break;
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }

        return discountQuantity;
    }

    private int handleExceedingPromotionStock(OrderProduct orderProduct, InputView inputView, OutputView outputView) {
        int discountQuantity = orderProduct.calculateDiscountQuantity();
        if (!handleInsufficientPromotionStock(orderProduct, inputView, outputView)) {
            return 0;
        }
        orderProduct.buyProduct();
        return discountQuantity;
    }

    private boolean handleInsufficientPromotionStock(OrderProduct orderProduct, InputView inputView, OutputView outputView) {
        while (true) {
            try {
                String userInput = inputView.readContinueBuy(orderProduct.getName(), orderProduct.getTotalBenefit());
                InputValidator.validateUserInput(userInput);

                if ("Y".equalsIgnoreCase(userInput)) {
                    return true;
                }
                if ("N".equalsIgnoreCase(userInput)) {
                    orderProduct.setResetQuantity();
                    return false;
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }

    private int processPromotion(OrderProduct orderProduct, InputView inputView, OutputView outputView) {
        int discountQuantity = orderProduct.calculateDiscountQuantity();
        orderProduct.buyProduct();

        if (orderProduct.hasAcquireBenefitPromotion() && orderProduct.hasPromotionOnDate()) {
            String userInput = inputView.readPromotionBenefit(orderProduct.getName(), orderProduct.getBenefitCount());
            InputValidator.validateUserInput(userInput);

            if ("Y".equalsIgnoreCase(userInput)) {
                orderProduct.applyPromotion(orderProduct.getBenefitCount());
                return orderProduct.calculateDiscountQuantity();
            }
        }

        return discountQuantity;
    }
}