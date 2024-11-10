package store.domain;

public record Membership(boolean isMember) {
    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    public int calculateDiscount(int remainingAmount) {
        if (!isMember) {
            return 0;
        }

        int discount = (int) (remainingAmount * DISCOUNT_RATE);
        return Math.min(discount, MAX_DISCOUNT);
    }
}
