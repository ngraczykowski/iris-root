package com.silenteight.warehouse.retention.production.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class EraseAlertsUseCase {

  @Language("PostgreSQL")
  private static final String DELETE_ALERTS_QUERY =
      "DELETE FROM warehouse_alert WHERE name IN (:names)";

  private static final String NAMES_PARAMETER = "names";

  private final int batchSize;

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  public void activate(List<String> alerts) {
    log.debug("Looking up for {} alerts to remove.", alerts.size());
    partition(alerts, batchSize).forEach(this::erase);
    log.debug("Removed {} alerts in total.", alerts.size());
  }

  private void erase(List<String> alertNames) {
    SqlParameterSource parameters = new MapSqlParameterSource(NAMES_PARAMETER, alertNames);
    jdbcTemplate.update(DELETE_ALERTS_QUERY, parameters);
  }
}
