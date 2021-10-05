package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@Qualifier("name")
class NameFeatureExtractor implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/name")
        .setAgentFeatureInput(Any.pack(NameFeatureInput
            .newBuilder()
            .setFeature("features/name")
            .setAlertedPartyType(learningMatch.getEntityType())
            .addAllMatchingTexts(learningMatch.getMatchingTexts())
            .addAllWatchlistNames(learningMatch
                .getWatchlistNames()
                .stream()
                .map(name -> WatchlistName.newBuilder().setName(name).build())
                .collect(toList()))
            .addAllAlertedPartyNames(
                learningMatch
                    .getAlertedPartyNames()
                    .stream()
                    .map(name -> AlertedPartyName.newBuilder().setName(name).build())
                    .collect(toList()))
            .build()))
        .build();
  }
}
