package store.view;

public class OutputView {
    private static final String HEADER_NOTICE = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_NOTICE = "현재 보유하고 있는 상품입니다.";

    public void printHeaderNotice() {
        System.out.println(HEADER_NOTICE);
    }

    public void printProductNotice() {
        System.out.println(PRODUCT_NOTICE);
        printWhiteSpace();
    }

    public void printResult(String result) {
        System.out.println(result);
    }

    private void printWhiteSpace() {
        System.out.println();
    }
}
