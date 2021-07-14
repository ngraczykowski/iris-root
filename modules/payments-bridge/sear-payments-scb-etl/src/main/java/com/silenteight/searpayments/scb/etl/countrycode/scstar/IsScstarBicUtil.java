package com.silenteight.searpayments.scb.etl.countrycode.scstar;

import lombok.NonNull;

class IsScstarBicUtil {

  private IsScstarBicUtil() {
  }

  static boolean isCorrect(@NonNull String bic) {
    if (bic.length() < 8) {
      return false;
    } else
      return areChartersCorrect(bic);
  }

  private static boolean areChartersCorrect(String bic) {
    for (var i = 0; i < bic.length(); ++i) {
      var character = bic.charAt(i);
      if (Character.isLetter(character)) {
        if (!Character.isUpperCase(character)) {
          return false;
        }
        continue;
      }
      if (!Character.isDigit(character)) {
        return false;
      }
    }
    return true;
  }
}
