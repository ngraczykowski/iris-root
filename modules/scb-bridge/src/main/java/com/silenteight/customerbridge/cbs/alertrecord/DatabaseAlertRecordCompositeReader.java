package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.alertid.AlertId;
import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;
import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.DecisionRecord;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

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

  @Override
  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  public AlertRecordCompositeCollection readWithCbsHitDetails(
      String dbRelationName, String cbsHitDetailsDbRelationName, List<AlertId> alertIds) {

    return buildCollection(dbRelationName, cbsHitDetailsDbRelationName, alertIds);
  }

  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  @Override
  public AlertRecordCompositeCollection read(String dbRelationName, List<AlertId> alertIds) {
    return buildCollection(dbRelationName, null, alertIds);
  }

  private AlertRecordCompositeCollection buildCollection(
      String dbRelationName,
      @Nullable String cbsHitDetailsDbRelationName,
      List<AlertId> alertIds) {

    Set<String> systemIds = alertIds.stream().map(AlertId::getSystemId).collect(toSet());
    List<AlertRecord> alertRecords = readAlertRecords(dbRelationName, systemIds);
    List<DecisionRecord> decisions = readDecisions(dbRelationName, systemIds);
    Map<AlertRecord, List<CbsHitDetails>> hitDetails =
        readHitDetails(cbsHitDetailsDbRelationName, alertRecords);

    return new AlertRecordCompositeCollection(
        alertIds,
        alertRecords,
        decisions,
        hitDetails
    );
  }

  private Map<AlertRecord, List<CbsHitDetails>> readHitDetails(
      @Nullable String cbsHitDetailsDbRelationName, List<AlertRecord> alertRecords) {

    if (isBlank(cbsHitDetailsDbRelationName))
      return Collections.emptyMap();

    var alertIds = alertRecords.stream()
        .filter(r -> nonNull(r.getBatchId()))
        .map(this::toAlertId)
        .collect(Collectors.toList());

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

  private List<DecisionRecord> readDecisions(String dbRelationName, Collection<String> systemIds) {
    return decisionRecordReader.read(dbRelationName, systemIds);
  }

  private List<AlertRecord> readAlertRecords(String dbRelationName, Collection<String> systemIds) {
    return alertRecordReader.read(dbRelationName, systemIds);
  }

  private List<CbsHitDetails> readCbsHitDetails(String dbRelationName, Collection<AlertId> ids) {
    return cbsHitDetailsReader.read(dbRelationName, ids);
  }
}
