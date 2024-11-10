package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.constants.ErrorMessage.ERROR_INVALID_FORMAT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.order.Orders;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotions;

class OrdersTest {
    private Products products;

    @BeforeEach
    void setUp() {
        products = new Products("products.md", new Promotions("promotions.md"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"[콜라-2]-[사이다-5]", "[콜라:10],[사이다/5]", "콜라-2,사이다-2", "[콜라-2-1]"})
    void 잘못된_형식의_입력시_예외발생(String invalidInput) {
        assertThatThrownBy(() -> new Orders(invalidInput, products))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_INVALID_FORMAT);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "[콜라-2],[사이다- 5]", "[콜라- 2],[사이다-5]", "[콜라-2], ", "[콜라-2],,[사이다-1]"})
    void 공백_입력시_예외발생(String invalidInput) {
        assertThatThrownBy(() -> new Orders(invalidInput, products))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_INVALID_FORMAT);
    }

    @Test
    void 여러_메뉴_정상적인_주문시_재고_감소() {
        String validOrderInput = "[콜라-3],[사이다-2]";
        Orders orders = new Orders(validOrderInput, products);

        Product cola = products.findProductsByName("콜라").get(0);
        Product cider = products.findProductsByName("사이다").get(0);

        assertThat(cola.getQuantity()).isEqualTo(cola.getQuantity());
        assertThat(cider.getQuantity()).isEqualTo(cider.getQuantity());
    }
}
