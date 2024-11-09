package store.controller;

import store.domain.Orders;
import store.domain.Products;
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
}
