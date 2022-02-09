package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.historicaldecisions.v2.*;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC;

class ContextualLearningFixture {

  static final String DISCRIMINATOR_PREFIX = "sierra";

  static ModelKey getModelKey(String ofacId, String watchlistType, String alertedPartyId) {
    var alertedParty = AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .build();

    var watchlistParty = WatchlistParty.newBuilder()
        .setId(ofacId)
        .setType(watchlistType)
        .build();

    var match = Match.newBuilder()
        .setAlertedParty(alertedParty)
        .setWatchlistParty(watchlistParty)
        .build();

    return ModelKey.newBuilder()
        .setMatch(match)
        .build();
  }

  static Discriminator getDiscriminator() {
    return Discriminator.newBuilder()
        .setValue(DISCRIMINATOR_PREFIX + "_" + CONTEXTUAL_LEARNING_DISC)
        .build();
  }
}
