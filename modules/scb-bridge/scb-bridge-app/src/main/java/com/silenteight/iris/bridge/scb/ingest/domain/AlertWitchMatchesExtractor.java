/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.domain.model.*;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class AlertWitchMatchesExtractor {

  AlertWithMatches extract(Alert alert) {
    List<Match> matches = toMatches(alert);
    if (matches.isEmpty()) {
      return extractWithFailure(alert, AlertErrorDescription.NO_MATCHES);
    }

    return AlertWithMatches.builder()
        .alertId(alert.id().sourceId())
        .status(AlertStatus.SUCCESS)
        .matches(matches)
        .metadata(createMetadata(alert))
        .build();
  }

  private AlertWithMatches extractWithFailure(Alert alert, AlertErrorDescription errorDescription) {
    return AlertWithMatches.builder()
        .alertId(alert.id().sourceId())
        .status(AlertStatus.FAILURE)
        .alertErrorDescription(errorDescription)
        .build();
  }

  private List<Match> toMatches(Alert alert) {
    return alert.matches()
        .stream()
        .map(match -> match.id().sourceId())
        .map(this::toMatch)
        .toList();
  }

  private Match toMatch(String matchId) {
    return Match.builder()
        .id(matchId)
        .build();
  }

  private AlertMetadata createMetadata(Alert alert) {
    return AlertMetadata.builder()
        .discriminator(alert.id().discriminator())
        .watchlistId(alert.details().getWatchlistId())
        .systemId(alert.details().getSystemId())
        .batchId(alert.details().getBatchId())
        .build();
  }
}
