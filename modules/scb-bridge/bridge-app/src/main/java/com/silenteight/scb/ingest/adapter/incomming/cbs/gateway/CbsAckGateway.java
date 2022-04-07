package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.AckCalledEvent;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLTransientException;
import javax.annotation.Nonnull;

import static com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State.TEMPORARY_FAILURE;
import static java.util.Objects.requireNonNull;

@Slf4j
public class CbsAckGateway extends CbsEventPublisher {

  private static final String TEMPLATE_VARIABLE = ":ackFunctionName";
  private final String query;
  private final JdbcTemplate jdbcTemplate;
  private final SourceApplicationValues sourceApplicationValues;

  CbsAckGateway(
      @NonNull String ackFunctionName,
      @NonNull JdbcTemplate jdbcTemplate,
      @NonNull SourceApplicationValues sourceApplicationValues) {
    this.query = prepareQuery(ackFunctionName);
    this.jdbcTemplate = jdbcTemplate;
    this.sourceApplicationValues = sourceApplicationValues;
  }

  private static String prepareQuery(String ackFunctionName) {
    return getQueryTemplate().replace(TEMPLATE_VARIABLE, ackFunctionName);
  }

  private static String getQueryTemplate() {
    return String.format("SELECT %s(?, ?, ?) FROM dual", TEMPLATE_VARIABLE);
  }

  @Timed(
      value = "serp.scb.bridge.oracle.procedure.ack-read-alert.time",
      description = "Time taken to ack read alert")
  @Transactional(value = "externalTransactionManager", readOnly = true)
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

  private void logAndNotifyError(CbsAckAlert alert, Exception exception) {
    log.error("CBS: Cannot acknowledge: systemId={}, batchId={}",
        alert.getAlertExternalId(), alert.getBatchId(), exception);

    notifyCbsCallFailed("ACK");
  }

  private void logAndNotifySuccess(CbsAckAlert alert, String result) {
    log.info("CBS: Acknowledge function executed: status={}, systemId={}, batchId={}",
        result, alert.getAlertExternalId(), alert.getBatchId());

    notifyAckCalled(result, alert.isWatchlistLevel());
  }

  private void notifyAckCalled(String statusCode, boolean watchlistLevel) {
    publishEvent(() -> AckCalledEvent
        .builder()
        .statusCode(statusCode)
        .watchlistLevel(watchlistLevel)
        .build());
  }

  @Nonnull
  private Object[] createParameters(CbsAckAlert alert) {
    return new Object[] {
        sourceApplicationValues.getSourceApplicationValue(alert.isWatchlistLevel()),
        alert.getAlertExternalId(),
        alert.getBatchId() };
  }
}
