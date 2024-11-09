package store.util;

import static store.constants.ErrorMessage.ERROR_GENERIC_INVALID_INPUT;

public class InputValidator {
    public static void validateUserInput(String userInput){
        if (!"Y".equals(userInput) && !"N".equals(userInput)) {
            throw new IllegalArgumentException(ERROR_GENERIC_INVALID_INPUT);
        }
    }
}
