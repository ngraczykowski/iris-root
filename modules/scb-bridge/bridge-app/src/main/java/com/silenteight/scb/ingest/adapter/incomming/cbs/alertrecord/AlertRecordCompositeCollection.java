package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.*;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertRecordCompositeCollection {

  private final List<AlertRecordComposite> alerts = new ArrayList<>();
  private final List<InvalidAlert> invalidAlerts = new ArrayList<>();

  AlertRecordCompositeCollection(
      List<AlertId> alertIds,
      List<AlertRecord> alertRecords,
      List<DecisionRecord> decisions,
      Map<AlertRecord, List<CbsHitDetails>> hitDetails) {

    Map<String, AlertRecord> groupedAlerts =
        alertRecords.stream().collect(toMap(AlertRecord::getSystemId, identity()));

    Map<String, List<DecisionRecord>> groupedDecisions =
        decisions.stream().collect(groupingBy(DecisionRecord::getSystemId));

    for (AlertId alertId : alertIds) {
      String systemId = alertId.getSystemId();
      String batchId = alertId.getBatchId();
      AlertRecord alertRecord = groupedAlerts.getOrDefault(systemId, null);

      if (alertRecord == null) {
        addInvalidAlert(systemId, batchId, Reason.ABSENT);
      } else if (!batchId.equals(alertRecord.getBatchId())) {
        addInvalidAlert(systemId, batchId, Reason.WRONG_BATCH_ID);
      } else {
        List<DecisionRecord> alertDecisions = groupedDecisions.getOrDefault(systemId, emptyList());
        List<CbsHitDetails> details = hitDetails.getOrDefault(alertRecord, emptyList());
        addAlert(alertRecord, alertDecisions, details);
      }
    }
  }

  private void addInvalidAlert(String systemId, String batchId, Reason reason) {
    invalidAlerts.add(new InvalidAlert(systemId, batchId, reason));
  }

  private void addAlert(
      AlertRecord alert, List<DecisionRecord> decisions, List<CbsHitDetails> cbsHitDetails) {

    alerts.add(AlertRecordComposite.builder()
        .alert(alert)
        .decisions(decisions)
        .cbsHitDetails(cbsHitDetails)
        .build());
  }

  List<String> getInvalidSystemIdsWithReason(Reason reason) {
    return invalidAlerts
        .stream()
        .filter(invalidAlert -> invalidAlert.getInvalidityReason() == reason)
        .map(InvalidAlert::getSystemId)
        .collect(toUnmodifiableList());
  }
}
