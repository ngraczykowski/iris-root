package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.ChineseCommercialCodesAgentResponse;
import com.silenteight.payments.bridge.agents.port.ChineseCommercialCodeUseCase;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.agents.model.ChineseCommercialCodesAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.ChineseCommercialCodesAgentResponse.YES;

@Service
class ChineseCommercialCodesAgent implements ChineseCommercialCodeUseCase {

  private static final String CHINESE_COMMERCIAL_CODES_PATTERN =
      "([12]/|\n|\\s)[0-9]{4}([12]/|\n|\\s)[0-9]{4}([12]/|\n|\\s)";
  private static final Pattern PATTERN = Pattern.compile(CHINESE_COMMERCIAL_CODES_PATTERN);

  @NonNull
  public ChineseCommercialCodesAgentResponse invoke(@NonNull String matchingField) {
    Matcher matcher = PATTERN.matcher(matchingField);
    return matcher.find() ? YES : NO;
  }
}
