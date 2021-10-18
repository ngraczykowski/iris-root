package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("identificationMismatch")
class IdentificationMismatchAgent implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/bankIdentificationCodes")
        .setAgentFeatureInput(Any.pack(BankIdentificationCodesFeatureInput
            .newBuilder()
            .setAlertedPartyMatchingField(learningMatch.getMatchedFieldValue())
            .setWatchlistMatchingText(StringUtils.join(learningMatch.getMatchingTexts(), ", "))
            .addAllWatchlistSearchCodes(learningMatch.getSearchCodes())
            .build()))
        .build();
  }
}
