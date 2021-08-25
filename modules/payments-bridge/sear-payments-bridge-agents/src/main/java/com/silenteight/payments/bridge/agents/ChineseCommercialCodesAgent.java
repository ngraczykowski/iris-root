package com.silenteight.payments.bridge.agents;

import lombok.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ChineseCommercialCodesAgent {

  private static final String CHINESE_COMMERCIAL_CODES_PATTERN =
      "([12]/|\n|\\s)[0-9]{4}([12]/|\n|\\s)[0-9]{4}([12]/|\n|\\s)";
  private static final Pattern PATTERN = Pattern.compile(CHINESE_COMMERCIAL_CODES_PATTERN);

  @NonNull
  public ChineseCommercialCodesAgentResponse invoke(@NonNull String matchingField) {
    Matcher matcher = PATTERN.matcher(matchingField);
    return matcher.find() ? ChineseCommercialCodesAgentResponse.YES
                          : ChineseCommercialCodesAgentResponse.NO;
  }
}
