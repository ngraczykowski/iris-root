package com.silenteight.warehouse.production.persistence.insert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.production.persistence.mapping.alert.AlertDefinition;
import com.silenteight.warehouse.production.persistence.mapping.match.MatchDefinition;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PersistenceService {

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @NonNull
  private final AlertPersistenceService alertPersistenceService;
  @NonNull
  private final LabelPersistenceService labelPersistenceService;
  @NonNull
  private final MatchPersistenceService matchPersistenceService;

  public void insert(AlertDefinition alertDefinition) {
    doInsert(alertDefinition);
  }

  private void doInsert(AlertDefinition alertDefinition) {
    log.debug(
        "Started process of persisting alert, discriminator={}",
        alertDefinition.getDiscriminator());

    long persistedAlertId = alertPersistenceService.persist(jdbcTemplate, alertDefinition);

    labelPersistenceService.persist(jdbcTemplate, persistedAlertId, alertDefinition.getLabels());

    List<MatchDefinition> matchDefinitions = alertDefinition.getMatchDefinitions();
    matchPersistenceService.persist(jdbcTemplate, persistedAlertId, matchDefinitions);
  }
}
