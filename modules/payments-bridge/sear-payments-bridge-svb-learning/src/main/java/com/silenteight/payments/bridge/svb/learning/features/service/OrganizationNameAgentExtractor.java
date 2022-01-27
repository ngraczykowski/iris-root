package com.silenteight.payments.bridge.svb.learning.features.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.ORGANIZATION_NAME_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Qualifier("organizationNameAgent")
class OrganizationNameAgentExtractor implements FeatureExtractor {


  @Override
  public List<FeatureInput> createFeatureInputs(LearningMatch learningMatch) {
    var nameFeatureInput = createNameFeatureInput(learningMatch);
    var featureInput = createFeatureInput(ORGANIZATION_NAME_FEATURE, nameFeatureInput);
    return List.of(featureInput);
  }

  private static NameFeatureInput createNameFeatureInput(LearningMatch learningMatch) {
    return NameFeatureInput.newBuilder()
        .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
        .addAllAlertedPartyNames(getAlertedParties(learningMatch))
        .addAllWatchlistNames(getWatchlistNames(learningMatch))
        .build();
  }

  private static List<WatchlistName> getWatchlistNames(LearningMatch learningMatch) {
    return learningMatch
        .getWatchlistNames()
        .stream()
        .map(OrganizationNameAgentExtractor::createWatchlistName)
        .collect(toList());
  }

  private static WatchlistName createWatchlistName(String watchlistName) {
    return WatchlistName.newBuilder()
        .setName(watchlistName)
        .build();
  }

  private static List<AlertedPartyName> getAlertedParties(LearningMatch learningMatch) {
    return learningMatch.getWatchlistType() == WatchlistType.COMPANY ? learningMatch
        .getAlertedPartyNames()
        .stream()
        .map(alertedPartyName -> AlertedPartyName
            .newBuilder()
            .setName(alertedPartyName)
            .build())
        .collect(toList()) : List.of();
  }
}
