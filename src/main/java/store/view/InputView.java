package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String ORDER_PRODUCT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String PROMOTION_BENEFIT_MESSAGE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String CONTINUE_BUY_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String USE_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String readOrderProduct() {
        System.out.println(ORDER_PRODUCT_MESSAGE);
        return Console.readLine();
    }

    public String readPromotionBenefit(String productName, Integer benefitCount) {
        System.out.printf(PROMOTION_BENEFIT_MESSAGE, productName, benefitCount);
        printWhiteSpace();
        return Console.readLine();
    }

    public String readContinueBuy(String productName, Integer benefitCount) {
        System.out.printf(CONTINUE_BUY_MESSAGE, productName, benefitCount);
        printWhiteSpace();
        return Console.readLine();
    }

    public String readUseMemberShip() {
        System.out.println(USE_MEMBERSHIP_MESSAGE);
        return Console.readLine();
    }

    public String readAdditionalPurchase() {
        System.out.println(ADDITIONAL_PURCHASE_MESSAGE);
        return Console.readLine();
    }

    private void printWhiteSpace() {
        System.out.println();
    }

    public void closeConsole() {
        Console.close();
    }
}
