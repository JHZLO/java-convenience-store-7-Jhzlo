package store.service;

import store.domain.Membership;
import store.view.InputView;
import store.view.OutputView;

public class MembershipService {
    public Membership handleMembership(InputView inputView, OutputView outputView) {
        while (true) {
            try {
                String userInput = inputView.readUseMemberShip();
                if ("Y".equalsIgnoreCase(userInput)) {
                    return new Membership(true);
                }
                if ("N".equalsIgnoreCase(userInput)) {
                    return new Membership(false);
                }
            } catch (IllegalArgumentException e) {
                outputView.printResult(e.getMessage());
            }
        }
    }
}
