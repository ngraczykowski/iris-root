package com.silenteight.scb.ingest.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.AlertErrorDescription;
import com.silenteight.scb.ingest.domain.model.AlertStatus;
import com.silenteight.scb.ingest.domain.model.AlertWithMatches;
import com.silenteight.scb.ingest.domain.model.Match;

import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AlertWitchMatchesExtractor {

  public AlertWithMatches extract(Alert alert) {
    List<Match> matches = toMatches(alert);
    if (matches.isEmpty()) {
      return extractWithFailure(alert, AlertErrorDescription.NO_MATCHES);
    }

    return AlertWithMatches.builder()
        .alertId(alert.id().sourceId())
        .status(AlertStatus.SUCCESS)
        .matches(matches)
        .build();
  }

  public AlertWithMatches extractWithFailure(Alert alert, AlertErrorDescription errorDescription) {
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
}
