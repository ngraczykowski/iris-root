package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("freeText")
class FreeTextExtractor implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/freeText")
        .setAgentFeatureInput(Any.pack(FreeTextFeatureInput
            .newBuilder()
            .setFeature("features/freeText")
            .setMatchedName(learningMatch.getWatchlistNames().get(0))
            .setMatchedType(learningMatch.getMatchType())
            .addAllMatchingTexts(learningMatch.getMatchingTexts())
            .setFreetext(learningMatch.getMatchedFieldValue())
            .build()))
        .build();
  }
}
