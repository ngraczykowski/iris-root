package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

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
class OrganizationNameAgentExtractorService implements FeatureExtractor {

  @Override
  public FeatureInput createFeatureInputs(EtlHit hit) {
    var nameFeatureInput = createNameFeatureInput(hit);
    return createFeatureInput(ORGANIZATION_NAME_FEATURE, nameFeatureInput);
  }

  @Override
  public String name() {
    return getFullFeatureName(ORGANIZATION_NAME_FEATURE);
  }

  private static NameFeatureInput createNameFeatureInput(EtlHit etlHit) {
    return NameFeatureInput.newBuilder()
        .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
        .addAllAlertedPartyNames(getAlertedParties(etlHit))
        .addAllWatchlistNames(getWatchlistNames(etlHit))
        .build();
  }

  private static List<WatchlistName> getWatchlistNames(EtlHit etlHit) {
    return etlHit
        .getWatchlistNames()
        .stream()
        .map(OrganizationNameAgentExtractorService::createWatchlistName)
        .collect(toList());
  }

  private static WatchlistName createWatchlistName(String watchlistName) {
    return WatchlistName.newBuilder()
        .setName(watchlistName)
        .build();
  }

  private static List<AlertedPartyName> getAlertedParties(EtlHit etlHit) {
    return etlHit.getWatchlistType() == WatchlistType.COMPANY ? etlHit
        .getAlertedPartyNames()
        .stream()
        .map(alertedPartyName -> AlertedPartyName
            .newBuilder()
            .setName(alertedPartyName)
            .build())
        .collect(toList()) : List.of();
  }
}
