package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.FEATURE_PREFIX;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;
import static java.util.stream.Collectors.toList;

@Component
class CreateNameFeatureInput implements CreateNameFeatureInputUseCase {

  @Override
  public NameFeatureInput createDefault(NameAgentRequest nameAgentRequest) {
    return createNameFeatureInput(nameAgentRequest);
  }

  @Override
  public NameFeatureInput createForOrganizationNameAgent(NameAgentRequest nameAgentRequest) {
    return createNameFeatureInputForOrganizationNameAgent(nameAgentRequest);
  }


  private static NameFeatureInput createNameFeatureInput(NameAgentRequest request) {
    return NameFeatureInput
        .newBuilder()
        .setFeature(getFeatureName(request.getFeature()))
        .addAllMatchingTexts(request.getMatchingTexts())
        .addAllWatchlistNames(createWatchlistNames(request))
        .addAllAlertedPartyNames(createAlertedPartyNames(request))
        .setAlertedPartyType(mapWatchListTypeToEntityType(request.getWatchlistType()))
        .build();
  }

  private static NameFeatureInput createNameFeatureInputForOrganizationNameAgent(
      NameAgentRequest nameAgentRequest) {
    var watchlistType = nameAgentRequest.getWatchlistType();
    var alertedPartyNames = nameAgentRequest.getAlertedPartyNames();
    var watchlistPartyNames = nameAgentRequest.getWatchlistNames().stream()
        .findFirst()
        .orElse("");

    if (watchlistType == WatchlistType.COMPANY) {
      return getNameFeatureInput(nameAgentRequest, alertedPartyNames, watchlistPartyNames);
    } else {
      return getNameFeatureInput(nameAgentRequest, new ArrayList<>(), watchlistPartyNames);
    }
  }

  @Nonnull
  private static NameFeatureInput getNameFeatureInput(
      NameAgentRequest nameAgentRequest, List<String> alertedPartyNames,
      String watchlistPartyNames) {
    return NameFeatureInput.newBuilder()
        .setFeature(getFullFeatureName(nameAgentRequest.getFeature()))
        .addAllAlertedPartyNames(getAlertedPartyNames(alertedPartyNames))
        .addWatchlistNames(WatchlistName.newBuilder()
            .setName(watchlistPartyNames)
            .build())
        .build();
  }

  @Nonnull
  private static List<AlertedPartyName> getAlertedPartyNames(List<String> alertedPartyNames) {
    return alertedPartyNames
        .stream()
        .map(alertedPartyName -> AlertedPartyName
            .newBuilder()
            .setName(alertedPartyName)
            .build())
        .collect(Collectors.toList());
  }

  private static List<WatchlistName> createWatchlistNames(NameAgentRequest request) {
    return request
        .getWatchlistNames()
        .stream()
        .map(CreateNameFeatureInput::createWatchlistName)
        .collect(toList());
  }

  private static WatchlistName createWatchlistName(String watchlistName) {
    return WatchlistName.newBuilder()
        .setName(watchlistName)
        .setType(NameType.REGULAR)
        .build();
  }

  private static List<AlertedPartyName> createAlertedPartyNames(NameAgentRequest request) {
    return request
        .getAlertedPartyNames()
        .stream()
        .map(CreateNameFeatureInput::createAlertedPartyName)
        .collect(toList());
  }

  private static AlertedPartyName createAlertedPartyName(String alertedPartyName) {
    return AlertedPartyName.newBuilder()
        .setName(alertedPartyName)
        .build();
  }

  @Nonnull
  private static EntityType mapWatchListTypeToEntityType(WatchlistType watchlistType) {
    switch (watchlistType) {
      case INDIVIDUAL:
        return EntityType.INDIVIDUAL;
      case COMPANY:
        return EntityType.ORGANIZATION;
      case ADDRESS:
      case VESSEL:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      default:
        throw new UnsupportedOperationException();
    }
  }

  private static String getFeatureName(String featureName) {
    return FEATURE_PREFIX + featureName;
  }

}
