package store.util;

import static store.constants.ErrorMessage.GENERIC_INVALID_INPUT;

public class InputValidator {
    public static void validateUserInput(String userInput) {
        if (!"Y".equals(userInput) && !"N".equals(userInput)) {
            throw new IllegalArgumentException(GENERIC_INVALID_INPUT.getMessage());
        }
    }
}
