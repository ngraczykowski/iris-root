package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Service
@Qualifier("name")
class NameFeatureExtractor implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    var watchlistNames = learningMatch
        .getWatchlistNames()
        .stream()
        .map(name -> WatchlistName.newBuilder().setName(name).setType(NameType.REGULAR).build())
        .collect(toList());

    var alertedPartyNames = learningMatch
        .getAlertedPartyNames()
        .stream()
        .map(name -> AlertedPartyName.newBuilder().setName(name).build())
        .collect(toList());

    return FeatureInput
        .newBuilder()
        .setFeature("features/name")
        .setAgentFeatureInput(Any.pack(NameFeatureInput
            .newBuilder()
            .setFeature("features/name")
            .setAlertedPartyType(learningMatch.getEntityType())
            .addAllMatchingTexts(learningMatch.getMatchingTexts())
            .addAllWatchlistNames(watchlistNames)
            .addAllAlertedPartyNames(alertedPartyNames)
            .setAlertedPartyType(mapWatchListTypeToEntityType(learningMatch.getWatchlistType()))
            .build()))
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
