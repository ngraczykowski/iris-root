package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.*;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Slf4j
class EcmAlertCompositeRowProcessor extends BaseAlertCompositeRowProcessor {

  private static final boolean WATCHLIST_LEVEL = true;

  EcmAlertCompositeRowProcessor(DateConverter dateConverter) {
    super(dateConverter);
  }

  List<AlertComposite> process(
      AlertRecord alertRecord,
      Map<ExternalId, List<Decision>> decisionsMap,
      SuspectsCollection suspects) {

    return suspects
        .streamAsSuspects()
        .filter(suspect -> decisionsMap.containsKey(getExternalId(alertRecord, suspect)))
        .map(suspect -> mapToAlertComposite(alertRecord, decisionsMap, suspect))
        .collect(Collectors.toList());
  }

  private AlertComposite mapToAlertComposite(
      AlertRecord alertRecord,
      Map<ExternalId, List<Decision>> decisionsMap,
      Suspect suspect) {

    var decisions = decisionsMap.get(getExternalId(alertRecord, suspect));
    var recordToAlertMapper = getRecordToAlertMapper(alertRecord, decisions);
    var suspects = new SuspectsCollection(singletonList(suspect));

    return AlertComposite.create(recordToAlertMapper, suspects, decisions.size());
  }

  private RecordToAlertMapper getRecordToAlertMapper(
      AlertRecord alertRecord, List<Decision> decisions) {
    var lastDecision = decisions.stream()
        .max(Comparator.comparing(Decision::createdAt))
        .orElseThrow(() -> new IllegalStateException("ECM decision does not exist"));
    var decisionCollection = new DecisionsCollection(List.of(lastDecision));

    return createRecordToAlertMapper(alertRecord, decisionCollection, WATCHLIST_LEVEL);
  }

  private static ExternalId getExternalId(AlertRecord alertRecord, Suspect suspect) {
    return new ExternalId(alertRecord.getSystemId(), suspect.getOfacId());
  }
}
