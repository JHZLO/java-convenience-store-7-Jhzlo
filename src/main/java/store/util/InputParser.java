package store.util;

import static store.constants.ErrorMessage.GENERIC_INVALID_INPUT;

public class InputParser {
    public static int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(GENERIC_INVALID_INPUT.getMessage());
        }
    }
}
