package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;
import static java.util.stream.Collectors.toList;

@Service
@Qualifier("name")
class NameFeatureExtractorService implements FeatureExtractor {

  private static final String NAME_FEATURE = "name";

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {
    var nameFeatureInput = createNameFeatureInput(etlHit);
    return createFeatureInput(NAME_FEATURE, nameFeatureInput);
  }

  private static NameFeatureInput createNameFeatureInput(EtlHit etlHit) {
    return NameFeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(NAME_FEATURE))
        .setAlertedPartyType(etlHit.getEntityType())
        .addAllMatchingTexts(etlHit.getMatchingTexts())
        .addAllWatchlistNames(createWatchlistNames(etlHit))
        .addAllAlertedPartyNames(createAlertedPartyNames(etlHit))
        .setAlertedPartyType(mapWatchListTypeToEntityType(etlHit.getWatchlistType()))
        .build();
  }

  private static List<WatchlistName> createWatchlistNames(EtlHit etlHit) {
    return etlHit
        .getWatchlistNames()
        .stream()
        .map(NameFeatureExtractorService::createWatchlistName)
        .collect(toList());
  }

  private static WatchlistName createWatchlistName(String watchlistName) {
    return WatchlistName.newBuilder()
        .setName(watchlistName)
        .setType(NameType.REGULAR)
        .build();
  }

  private static List<AlertedPartyName> createAlertedPartyNames(EtlHit etlHit) {
    return etlHit
        .getAlertedPartyNames()
        .stream()
        .map(NameFeatureExtractorService::createAlertedPartyName)
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
}
