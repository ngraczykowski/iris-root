package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.historicaldecisions.v2.AlertedParty;
import com.silenteight.datasource.api.historicaldecisions.v2.Match;
import com.silenteight.datasource.api.historicaldecisions.v2.ModelKey;
import com.silenteight.datasource.api.historicaldecisions.v2.WatchlistParty;

@Value
@Builder
public class ContextualLearningAgentRequest {

  String ofacId;

  String watchlistType;

  String matchingField;

  String matchText;

  public ModelKey createModelKey(String alertedPartyId) {
    return ModelKey.newBuilder()
        .setMatch(createMatch(alertedPartyId))
        .build();
  }

  private Match createMatch(String alertedPartyId) {
    return Match.newBuilder()
        .setAlertedParty(createAlertedParty(alertedPartyId))
        .setWatchlistParty(createWatchlistParty())
        .build();
  }

  private static AlertedParty createAlertedParty(String alertedPartyId) {
    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .build();
  }

  private WatchlistParty createWatchlistParty() {
    return WatchlistParty.newBuilder()
        .setId(ofacId)
        .setType(watchlistType)
        .build();
  }

}
