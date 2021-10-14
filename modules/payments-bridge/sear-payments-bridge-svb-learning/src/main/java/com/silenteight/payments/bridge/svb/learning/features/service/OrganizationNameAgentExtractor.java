package com.silenteight.payments.bridge.svb.learning.features.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("organizationNameAgent")
class OrganizationNameAgentExtractor implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/organizationName")
        .setAgentFeatureInput(Any.pack(NameFeatureInput.newBuilder()
            .setFeature("organizationName")
            .addAllAlertedPartyNames(getAlertedParties(learningMatch))
            .addAllWatchlistNames(learningMatch
                .getWatchlistNames()
                .stream()
                .map(n -> WatchlistName.newBuilder().setName(n).build())
                .collect(
                    Collectors.toList()))
            .build()))
        .build();
  }

  static List<AlertedPartyName> getAlertedParties(LearningMatch learningMatch) {
    return learningMatch.getWatchlistType() == WatchlistType.COMPANY ? learningMatch
        .getAlertedPartyNames()
        .stream()
        .map(alertedPartyName -> AlertedPartyName
            .newBuilder()
            .setName(alertedPartyName)
            .build())
        .collect(Collectors.toList()) : List.of();
  }
}
