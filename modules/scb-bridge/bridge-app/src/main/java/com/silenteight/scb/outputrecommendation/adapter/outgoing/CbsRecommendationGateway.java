package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsEventPublisher;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.SourceApplicationValues;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

@Slf4j
public class CbsRecommendationGateway extends CbsEventPublisher {

  private static final String TEMPLATE_VARIABLE = ":recomFunctionName";
  private final JdbcTemplate jdbcTemplate;
  private final String query;
  private final SourceApplicationValues sourceApplicationValues;

  public CbsRecommendationGateway(
      @NonNull String recomFunctionName,
      @NonNull JdbcTemplate jdbcTemplate,
      @NonNull SourceApplicationValues sourceApplicationValues) {
    this.jdbcTemplate = jdbcTemplate;
    this.query = prepareQuery(recomFunctionName);
    this.sourceApplicationValues = sourceApplicationValues;
  }

  private static String prepareQuery(String recomFunctionName) {
    return getQueryTemplate().replace(TEMPLATE_VARIABLE, recomFunctionName);
  }

  private static String getQueryTemplate() {
    return String.format("SELECT %s(?, ?, ?, ?, ?, ?, ?, ?) FROM dual", TEMPLATE_VARIABLE);
  }

  @Transactional(value = "externalTransactionManager", readOnly = true)
  void recommendAlerts(List<CbsAlertRecommendation> alertsToBeRecommended) {
    alertsToBeRecommended.forEach(this::recommendAlert);
  }

  @Timed(
      value = "serp.scb.bridge.oracle.procedure.recommend-alert.time",
      description = "Time taken to call recommend function")
  void recommendAlert(@NonNull CbsAlertRecommendation alertRecommendation) {
    try {
      String statusCode = executeRecomFunction(alertRecommendation);

      log.info(
          "CBS: Recommendation function executed: status={}, systemId={}, hitWatchlistId={},"
              + " batchId={}",
          statusCode,
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getHitWatchlistId(),
          alertRecommendation.getBatchId());

      notifyRecomCalled(statusCode);
    } catch (Exception e) {
      log.error(
          "CBS: Cannot recommend given: systemId={}, batchId={}",
          alertRecommendation.getAlertExternalId(),
          alertRecommendation.getBatchId(),
          e);
      notifyCbsCallFailed("RECOM");
    }
  }

  private String executeRecomFunction(@NonNull CbsAlertRecommendation alertRecommendation) {
    Object[] parameters = createParameters(alertRecommendation);

    return requireNonNull(jdbcTemplate.queryForObject(query, parameters, String.class));
  }

  @Nonnull
  private Object[] createParameters(CbsAlertRecommendation alertRecommendation) {
    return new Object[] {
        sourceApplicationValues.getSourceApplicationValue(alertRecommendation.isWatchlistLevel()),
        alertRecommendation.getAlertExternalId(),
        alertRecommendation.getBatchId(),
        alertRecommendation.getHitWatchlistId(),
        alertRecommendation.getHitRecommendedStatus(),
        createClob(alertRecommendation.getHitRecommendedComments()),
        alertRecommendation.getListRecommendedStatus(),
        createClob(alertRecommendation.getListRecommendedComments())
    };
  }

  private static SqlParameterValue createClob(String text) {
    return new SqlParameterValue(Types.CLOB, new SqlLobValue(text));
  }

  private void notifyRecomCalled(String statusCode) {
    publishEvent(() -> RecomCalledEvent
        .builder()
        .statusCode(statusCode)
        .build());
  }
}
