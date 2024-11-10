package store.controller;

import store.domain.Receipt;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final StoreService storeService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView, StoreService storeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                outputView.printHeaderNotice();
                outputView.printProductNotice();
                outputView.printResult(storeService.getProductList());

                Receipt receipt = storeService.processStore(inputView, outputView);
                System.out.println(receipt);

                flag = handleAdditionalPurchase();
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
        inputView.closeConsole();
    }

    private boolean handleAdditionalPurchase() {
        String userInput = inputView.readAdditionalPurchase();
        return "Y".equalsIgnoreCase(userInput);
    }
}
