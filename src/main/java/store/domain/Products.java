package store.domain;

import static store.constants.ErrorMessage.ERROR_NON_EXISTENT_PRODUCT;
import static store.constants.ErrorMessage.ERROR_QUANTITY_EXCEEDS_STOCK;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import store.util.FileReader;
import store.util.InputParser;

public class Products {
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;
    private static final int PRODUCTS_LENGTH = 3;

    private final List<Product> products;

    public Products(String fileName, Promotions promotions) {
        this.products = new ArrayList<>();
        addProduct(FileReader.readFileData(fileName), promotions);
    }

    public void addProduct(List<String> productData, Promotions promotions) {
        for (String data : productData) {
            String[] parts = data.split(",");
            String name = parts[NAME_INDEX];
            int price = InputParser.parseInt(parts[PRICE_INDEX]);
            int quantity = InputParser.parseInt(parts[QUANTITY_INDEX]);
            String promotionName = null;
            if (hasPromotionValue(parts)) {
                promotionName = parts[PROMOTION_INDEX].trim();
            }
            products.add(new Product(name, price, quantity, promotions.findPromotionByName(promotionName)));
        }
    }

    public List<Product> findProductsByName(String productName) {
        List<Product> matchingProducts = products.stream()
                .filter(product -> product.getName().equals(productName))
                .collect(Collectors.toList());

        if (matchingProducts.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NON_EXISTENT_PRODUCT);
        }

        return matchingProducts;
    }

    private boolean hasPromotionValue(String[] parts) {
        return parts.length > PRODUCTS_LENGTH && !parts[PROMOTION_INDEX].equals("null");
    }

    public String getProductsAsString() {
        StringBuilder result = new StringBuilder();
        for (Product product : products) {
            result.append(product.toString()).append("\n");
        }
        return result.toString();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
