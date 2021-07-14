package com.silenteight.searpayments.scb.etl.countrycode.scstar;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

class ExtractCountryFromScstarBicImpl implements ExtractCountryFromScstarBic {

  @Override
  public @NonNull String invoke(@NonNull String bic) {
    if (IsScstarBicUtil.isCorrect(bic)) {
      return getCountryCode(bic);
    } else {
      throw new ScstarBicCodeFormatException(bic);
    }

  }

  private static String getCountryCode(String bic) {
    if (bic.startsWith("GB", 4) && bic.endsWith("SCO")) {
      return "JE";
    } else if (bic.startsWith("MY", 4) && bic.endsWith("LAB")) {
      return "LN";
    } else if (bic.startsWith("SCBLTWTX")) {
      return "TE";
    } else if (bic.startsWith("SCBLAE") && endsWithDifcChars(bic)) {
      return "DF";
    } else {
      return bic.substring(4, 6);
    }
  }

  private static boolean endsWithDifcChars(String unitCode) {
    List<String> charsForDifc = Arrays.asList("DIF", "DFM", "ADX", "NAS", "BAH", "OMA", "QAR",
        "JOR", "BOT", "CDI", "GHA", "KEN", "MUR", "NIG", "PAK", "TAN", "UGA", "ZAM",
        "SAU", "NAM", "VND");
    return charsForDifc.stream()
        .anyMatch(unitCode::endsWith);
  }
}
