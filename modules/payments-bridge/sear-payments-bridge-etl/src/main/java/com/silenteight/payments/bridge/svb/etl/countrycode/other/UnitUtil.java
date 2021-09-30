package com.silenteight.payments.bridge.svb.etl.countrycode.other;

public class UnitUtil {

  private UnitUtil() {
  }

  static boolean isValid(String unit) {
    String[] split = unit.split("-");
    if (split.length == 2) {
      return areChartersCorrect(split[0]) &&
          isLengthOfCountryCodeValid(split[1]) &&
          areChartersCorrect((split[1]));
    } else {
      return false;
    }
  }

  private static boolean isLengthOfCountryCodeValid(String countryCode) {
    return countryCode.length() == 2 || countryCode.length() == 3;
  }

  private static boolean areChartersCorrect(String bic) {
    if (bic.isBlank())
      return false;
    for (var i = 0; i < bic.length(); ++i) {
      var letter = bic.charAt(i);
      if (Character.isLetter(letter)) {
        if (!Character.isUpperCase(letter)) {
          return false;
        }
        continue;
      }
      return false;
    }
    return true;
  }
}
