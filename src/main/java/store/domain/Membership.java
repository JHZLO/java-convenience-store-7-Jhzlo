package store.domain;

public class Membership {
    private static final double DISCOUNT_RATE = 0.3; 
    private static final int MAX_DISCOUNT = 8000;

    private final boolean isMember;

    public Membership(boolean isMember) {
        this.isMember = isMember;
    }

    public boolean isMember() {
        return isMember;
    }

    public int calculateDiscount(int remainingAmount) {
        if (!isMember) {
            return 0;
        }

        int discount = (int) (remainingAmount * DISCOUNT_RATE);
        return Math.min(discount, MAX_DISCOUNT);
    }
}
