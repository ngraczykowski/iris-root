package com.silenteight.customerbridge.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.batch.*;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.proto.serp.v1.alert.Decision;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;
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

  private static ExternalId getExternalId(AlertRecord alertRecord, Suspect suspect) {
    return new ExternalId(alertRecord.getSystemId(), suspect.getOfacId());
  }

  private RecordToAlertMapper getRecordToAlertMapper(
      AlertRecord alertRecord, List<Decision> decisions) {
    var lastDecision = decisions.stream()
        .max(Comparator.comparing(d -> d.getCreatedAt().getNanos()))
        .orElseThrow(() -> new IllegalStateException("ECM decision does not exist"));
    var decisionCollection = new DecisionsCollection(singleton(lastDecision));

    return createRecordToAlertMapper(alertRecord, decisionCollection, WATCHLIST_LEVEL);
  }
}
