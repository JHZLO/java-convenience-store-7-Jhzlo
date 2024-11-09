package store.controller;

import store.view.InputView;
import store.view.OutputView;
import store.model.Promotions;

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
    }
}
