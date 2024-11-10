package store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MembershipTest {
    @Test
    void 멤버십_회원일때_할인금액_계산() {
        Membership membership = new Membership(true);

        int discount = membership.calculateDiscount(30000);
        assertEquals(8000, discount);

        discount = membership.calculateDiscount(20000);
        assertEquals(6000, discount);
    }

    @Test
    void 멤버십_비회원일때_할인금액_계산() {
        Membership membership = new Membership(false);

        int discount = membership.calculateDiscount(30000);
        assertEquals(0, discount);
    }

    @Test
    void 멤버십_회원일때_최대할인_적용() {
        Membership membership = new Membership(true);

        int discount = membership.calculateDiscount(40000);
        assertEquals(8000, discount); // 최대 할인 금액은 8000원
    }

    @Test
    void 멤버십_회원여부_확인() {
        Membership membership = new Membership(true);
        assertTrue(membership.isMember());

        Membership nonMember = new Membership(false);
        assertFalse(nonMember.isMember());
    }
}
