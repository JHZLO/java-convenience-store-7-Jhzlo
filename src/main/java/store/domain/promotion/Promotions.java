package store.domain.promotion;

import java.util.ArrayList;
import java.util.List;
import store.util.FileReader;
import store.util.InputParser;

public class Promotions {
    private static final int NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_QUANTITY_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    private final List<Promotion> promotions;

    public Promotions(String fileName) {
        this.promotions = new ArrayList<>();
        addPromotionData(FileReader.readFileData(fileName));
    }

    public void addPromotionData(List<String> promotionData) {
        for (String data : promotionData) {
            String[] parts = data.split(",");
            String name = parts[NAME_INDEX];
            int buy = InputParser.parseInt(parts[BUY_INDEX]);
            int get = InputParser.parseInt(parts[GET_QUANTITY_INDEX]);
            String startDate = parts[START_DATE_INDEX];
            String endDate = parts[END_DATE_INDEX];
            promotions.add(new Promotion(name, buy, get, startDate, endDate));
        }
    }

    public Promotion findPromotionByName(String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.isMatchPromotion(promotionName)) {
                return promotion;
            }
        }
        return null;
    }
}
