package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.metrics.CbsOracleMetrics;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import io.micrometer.core.instrument.Timer;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@RequiredArgsConstructor
@Slf4j
class DatabaseAlertRecordCompositeReader implements AlertRecordCompositeReader {

  private final DatabaseAlertRecordReader alertRecordReader;
  private final DatabaseDecisionRecordReader decisionRecordReader;
  private final DatabaseCbsHitDetailsReader cbsHitDetailsReader;
  private final CbsOracleMetrics cbsOracleMetrics;

  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  @Override
  public AlertRecordCompositeCollection read(
      ScbAlertIdContext scbAlertIdContext, List<AlertId> alertIds) {
    String sourceView = scbAlertIdContext.getSourceView();
    String cbsHitDetailsView = scbAlertIdContext.getHitDetailsView();

    Set<String> systemIds = alertIds.stream().map(AlertId::getSystemId).collect(toSet());
    List<AlertRecord> alertRecords = readAlertRecords(sourceView, systemIds);
    List<DecisionRecord> decisions = readDecisions(sourceView, systemIds);
    Map<AlertRecord, List<CbsHitDetails>> hitDetails =
        readHitDetails(cbsHitDetailsView, alertRecords);

    return new AlertRecordCompositeCollection(
        alertIds,
        alertRecords,
        decisions,
        hitDetails,
        scbAlertIdContext
    );
  }

  private Map<AlertRecord, List<CbsHitDetails>> readHitDetails(
      @Nullable String cbsHitDetailsDbRelationName, List<AlertRecord> alertRecords) {

    if (isBlank(cbsHitDetailsDbRelationName))
      return Collections.emptyMap();

    var alertIds = alertRecords.stream()
        .filter(r -> nonNull(r.getBatchId()))
        .map(this::toAlertId)
        .toList();

    var cbsHitDetails = readCbsHitDetails(cbsHitDetailsDbRelationName, alertIds);
    return alertRecords.stream().collect(toMap(
        a -> a,
        a -> collectCbsHitDetails(a, cbsHitDetails)
    ));
  }

  private List<CbsHitDetails> collectCbsHitDetails(
      AlertRecord alertRecord, List<CbsHitDetails> cbsHitDetails) {
    return cbsHitDetails.stream()
        .filter(r -> r.getSystemId().equals(alertRecord.getSystemId()))
        .collect(Collectors.toUnmodifiableList());
  }

  private AlertId toAlertId(AlertRecord r) {
    return AlertId.builder()
        .batchId(r.getBatchId())
        .systemId(r.getSystemId())
        .build();
  }

  private List<CbsHitDetails> readCbsHitDetails(
      String dbRelationName, Collection<AlertId> alertIds) {
    return timed(
        cbsOracleMetrics.cbsHitDetailsReaderTimer(dbRelationName),
        () -> cbsHitDetailsReader.read(dbRelationName, alertIds),
        format("CbsHitDetails read from: %s for %s alerts", dbRelationName, alertIds.size()));
  }

  private List<DecisionRecord> readDecisions(String dbRelationName, Collection<String> systemIds) {
    return timed(
        cbsOracleMetrics.decisionsReaderTimer(dbRelationName),
        () -> decisionRecordReader.read(dbRelationName, systemIds),
        format("DecisionRecord read from: %s for %s alerts", dbRelationName, systemIds.size()));
  }

  private List<AlertRecord> readAlertRecords(String dbRelationName, Collection<String> systemIds) {
    return timed(
        cbsOracleMetrics.recordReaderTimer(dbRelationName),
        () -> alertRecordReader.read(dbRelationName, systemIds),
        format("AlertRecord read from: %s for %s alerts", dbRelationName, systemIds.size()));
  }

  private static <T> List<T> timed(
      Timer timer,
      Supplier<List<T>> supplier,
      String logInfo) {
    var stopWatch = StopWatch.createStarted();
    var result = timer.record(supplier);
    log.info("{} executed in: {}, result size: {}", logInfo, stopWatch, result.size());
    return result;
  }

}
