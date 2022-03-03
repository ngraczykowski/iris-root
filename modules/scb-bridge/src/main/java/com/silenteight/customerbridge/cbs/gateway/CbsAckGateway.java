package com.silenteight.customerbridge.cbs.gateway;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.gateway.CbsOutput.State;
import com.silenteight.customerbridge.cbs.gateway.event.AckCalledEvent;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLTransientException;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

import static com.silenteight.customerbridge.cbs.gateway.CbsOutput.State.TEMPORARY_FAILURE;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Slf4j
public class CbsAckGateway extends CbsEventPublisher {

  private final String query;
  private final JdbcTemplate jdbcTemplate;
  private final SourceApplicationValues sourceApplicationValues;

  private static final String TEMPLATE_VARIABLE = ":ackFunctionName";

  CbsAckGateway(
      @NonNull String ackFunctionName,
      @NonNull JdbcTemplate jdbcTemplate,
      @NonNull SourceApplicationValues sourceApplicationValues) {
    this.query = prepareQuery(ackFunctionName);
    this.jdbcTemplate = jdbcTemplate;
    this.sourceApplicationValues = sourceApplicationValues;
  }

  @Timed(
      value = "serp.scb.bridge.oracle.procedure.ack-read-alerts.time",
      description = "Time taken to ack read alerts")
  @Transactional(value = "externalTransactionManager", readOnly = true)
  public List<CbsOutput> ackReadAlerts(@NonNull Set<CbsAckAlert> alerts) {
    return alerts.stream()
        .map(this::ackReadAlert)
        .collect(toList());
  }

  public CbsOutput ackReadAlert(@NonNull CbsAckAlert alert) {
    CbsOutput cbsOutput = new CbsOutput();

    try {
      String result = jdbcTemplate.queryForObject(query, createParameters(alert), String.class);

      requireNonNull(result);
      logAndNotifySuccess(alert, result);
      cbsOutput.setStatusCode(result);
    } catch (TransientDataAccessException e) {
      markAndLogNonFatalException(alert, cbsOutput, e);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLTransientException)
        markAndLogNonFatalException(alert, cbsOutput, e);
      else
        markAndLogFatalException(alert, cbsOutput, e);
    }

    return cbsOutput;
  }

  private void markAndLogFatalException(
      CbsAckAlert alert, CbsOutput cbsOutput, Exception exception) {
    cbsOutput.setState(State.ERROR);
    logAndNotifyError(alert, exception);
  }

  private void markAndLogNonFatalException(
      CbsAckAlert alert, CbsOutput cbsOutput, Exception exception) {
    cbsOutput.setState(TEMPORARY_FAILURE);
    logAndNotifyError(alert, exception);
  }

  private void logAndNotifySuccess(CbsAckAlert alert, String result) {
    log.info("CBS: Acknowledge function executed: status={}, systemId={}, batchId={}",
        result, alert.getAlertExternalId(), alert.getBatchId());

    notifyAckCalled(result, alert.isWatchlistLevel());
  }

  private void logAndNotifyError(CbsAckAlert alert, Exception exception) {
    log.error("CBS: Cannot acknowledge: systemId={}, batchId={}",
        alert.getAlertExternalId(), alert.getBatchId(), exception);

    notifyCbsCallFailed("ACK");
  }

  @Nonnull
  private Object[] createParameters(CbsAckAlert alert) {
    return new Object[] {
        sourceApplicationValues.getSourceApplicationValue(alert.isWatchlistLevel()),
        alert.getAlertExternalId(),
        alert.getBatchId() };
  }

  private void notifyAckCalled(String statusCode, boolean watchlistLevel) {
    publishEvent(() -> AckCalledEvent
        .builder()
        .statusCode(statusCode)
        .watchlistLevel(watchlistLevel)
        .build());
  }

  private static String prepareQuery(String ackFunctionName) {
    return getQueryTemplate().replace(TEMPLATE_VARIABLE, ackFunctionName);
  }

  private static String getQueryTemplate() {
    return "SELECT " + TEMPLATE_VARIABLE + "(?, ?, ?) FROM dual";
  }
}
