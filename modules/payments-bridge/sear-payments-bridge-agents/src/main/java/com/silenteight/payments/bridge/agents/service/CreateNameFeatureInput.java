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

import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.FEATURE_PREFIX;
import static java.util.stream.Collectors.toList;

@Component
class CreateNameFeatureInput implements CreateNameFeatureInputUseCase {

  @Override
  public NameFeatureInput create(NameAgentRequest nameAgentRequest) {
    return createNameFeatureInput(nameAgentRequest);
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
