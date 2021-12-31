package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.features.service.AgentExtractorUtils.createFeatureInput;

@Service
@Qualifier("identificationMismatch")
class IdentificationMismatchAgent implements FeatureExtractor {

  private static final String BANK_IDENTIFICATION_CODES_FEATURE = "bankIdentificationCodes";

  @Override
  public List<FeatureInput> createFeatureInputs(LearningMatch learningMatch) {
    var bankIdentificationCodesFeatureInput =
        createBankIdentificationCodesFeatureInput(learningMatch);
    var featureInput =
        createFeatureInput(BANK_IDENTIFICATION_CODES_FEATURE, bankIdentificationCodesFeatureInput);
    return List.of(featureInput);
  }

  private static BankIdentificationCodesFeatureInput createBankIdentificationCodesFeatureInput(
      LearningMatch learningMatch) {
    return BankIdentificationCodesFeatureInput
        .newBuilder()
        .setAlertedPartyMatchingField(learningMatch.getMatchedFieldValue())
        .setWatchlistMatchingText(StringUtils.join(learningMatch.getMatchingTexts(), ", "))
        .addAllWatchlistSearchCodes(learningMatch.getSearchCodes())
        .build();
  }
}
