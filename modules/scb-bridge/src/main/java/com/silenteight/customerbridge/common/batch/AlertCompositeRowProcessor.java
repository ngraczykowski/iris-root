package com.silenteight.customerbridge.common.batch;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.alertmapper.GnsMatchCalculator;
import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Slf4j
class AlertCompositeRowProcessor extends BaseAlertCompositeRowProcessor {

  private final boolean watchlistLevel;

  AlertCompositeRowProcessor(
      DateConverter dateConverter, boolean watchlistLevel) {
    super(dateConverter);
    this.watchlistLevel = watchlistLevel;
  }

  List<AlertComposite> process(
      AlertRecord alertRecord, SuspectsCollection suspects, DecisionsCollection decisions) {

    RecordToAlertMapper recordToAlertMapper =
        createRecordToAlertMapper(alertRecord, decisions, watchlistLevel);
    return watchlistLevel ?
           processOnWatchlistLevel(suspects, recordToAlertMapper) :
           processOnAlertLevel(suspects, recordToAlertMapper);
  }

  private static List<AlertComposite> processOnWatchlistLevel(
      SuspectsCollection suspects, RecordToAlertMapper alertMapper) {

    List<AlertComposite> alertComposites = new ArrayList<>();

    if (suspects.hasSuspects()) {
      List<AlertComposite> mappedAlertComposites = suspects
          .streamAsSuspects()
          .filter(it -> shouldSuspectBeProcessed(alertMapper, it))
          .map(it -> mapToAlertComposite(alertMapper, it))
          .collect(Collectors.toList());

      alertComposites.addAll(mappedAlertComposites);
    }

    if (alertComposites.isEmpty()) {
      log.warn(
          "Record: {} without new hits. Will not be processed and acknowledged",
          alertMapper.getSystemId());
    }

    if (log.isDebugEnabled())
      log.debug("Mapped on watchlist level: alerts={}", alertComposites.size());

    return alertComposites;
  }

  private static boolean shouldSuspectBeProcessed(
      RecordToAlertMapper alertMapper, Suspect suspect) {
    GnsMatchCalculator gnsMatchCalculator = new GnsMatchCalculator(
        suspect,
        alertMapper.getAlertData().getLastDecBatchId(),
        alertMapper.getDecisionsCollection().hasLastDecision());

    return gnsMatchCalculator.isNew();
  }

  private static AlertComposite mapToAlertComposite(
      RecordToAlertMapper recordToAlertMapper,
      Suspect suspect) {

    SuspectsCollection suspects = new SuspectsCollection(singletonList(suspect));
    return AlertComposite.create(recordToAlertMapper, suspects);
  }

  private static List<AlertComposite> processOnAlertLevel(
      SuspectsCollection suspects,
      RecordToAlertMapper recordToAlertMapper) {
    List<AlertComposite> result = new ArrayList<>();

    result.add(AlertComposite.create(recordToAlertMapper, suspects));

    if (log.isDebugEnabled())
      log.debug("Mapped on alert level: suspects={}", suspects.size());

    return result;
  }
}
