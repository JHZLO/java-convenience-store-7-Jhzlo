package store.domain.product;

import static store.constants.ErrorMessage.NON_EXISTENT_PRODUCT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import store.domain.promotion.Promotion;
import store.domain.promotion.Promotions;
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
                promotionName = parts[PROMOTION_INDEX];
            }
            products.add(new Product(name, price, quantity, promotions.findPromotionByName(promotionName)));
        }
    }

    public void addProductWithoutPromotion() {
        Set<String> processedNames = new HashSet<>();

        List<Product> originalProducts = new ArrayList<>(products);

        for (Product product : originalProducts) {
            String productName = product.getName();

            if (processedNames.contains(productName)) {
                continue;
            }
            List<Product> sameNameProducts = findProductsByName(productName);

            boolean hasOnlyPromotionalStock = sameNameProducts.size() == 1 && product.hasPromotion();
            if (hasOnlyPromotionalStock) {
                products.add(new Product(productName, product.getPrice(), 0, null));
            }
            processedNames.add(productName);
        }
    }


    public List<Product> findProductsByName(String productName) {
        List<Product> matchingProducts = products.stream()
                .filter(product -> product.getName().equals(productName))
                .collect(Collectors.toList());

        if (matchingProducts.isEmpty()) { // 유효성 검사
            throw new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage());
        }

        return matchingProducts;
    }

    private boolean hasPromotionValue(String[] parts) {
        return parts.length > PRODUCTS_LENGTH && !parts[PROMOTION_INDEX].equals("null");
    }

    public Promotion getPromotionByName(String productName) { // name으로 프로모션을 가지고 있는지 조회
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                if (product.hasPromotion()) {
                    return product.getPromotion();
                }
            }
        }
        return null;
    }

    private Map<String, List<Product>> groupProductsByName(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName));
    }

    public String getProductsAsString() {
        StringBuilder result = new StringBuilder();
        Map<String, List<Product>> groupedProducts = groupProductsByName(products);

        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            List<Product> productList = entry.getValue();

            for (Product product : productList) {
                String promotionText = getPromotionText(product);
                String quantityText = getQuantityText(product);

                result.append(String.format("- %s %,d원 %s %s\n",
                        product.getName(),
                        product.getPrice(),
                        quantityText,
                        promotionText
                ));
            }
        }

        return result.toString();
    }

    private String getPromotionText(Product product) {
        if (product.hasPromotion()) {
            return product.getPromotion().getPromotionName();
        }
        return "";
    }

    private String getQuantityText(Product product) {
        if (product.getQuantity() > 0) {
            return product.getQuantity() + "개";
        }
        return "재고 없음";
    }
}
