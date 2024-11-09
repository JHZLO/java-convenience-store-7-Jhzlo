package store.view;

public class OutputView {
    public void printHeaderNotice() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printProductNotice() {
        System.out.println("현재 보유하고 있는 상품입니다.");
        printWhiteSpace();
    }

    public void printResult(String result) {
        System.out.println(result);
    }

    private void printWhiteSpace() {
        System.out.println();
    }
}
