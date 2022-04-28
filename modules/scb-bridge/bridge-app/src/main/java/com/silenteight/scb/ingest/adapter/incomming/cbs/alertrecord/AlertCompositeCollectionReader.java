package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper.Option;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason;
import com.silenteight.scb.ingest.adapter.incomming.cbs.metrics.CbsOracleMetrics;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
class AlertCompositeCollectionReader {

  private final AlertMapper alertMapper;
  private final DatabaseAlertRecordCompositeReader databaseAlertRecordCompositeReader;
  private final boolean processOnlyUnsolved;
  private final CbsOracleMetrics cbsOracleMetrics;

  AlertCompositeCollection read(
      String internalBatchId, ScbAlertIdContext context, List<AlertId> alertIds) {
    var alertCollection = getAlertCollection(alertIds, context);
    var options = getMappingOptions(context);
    var invalidAlerts = new ArrayList<>(alertCollection.getInvalidAlerts());

    log.info("Parsing {} alerts", alertIds.size());
    var stopWatch = StopWatch.createStarted();

    var alerts = alertCollection.getAlerts()
        .stream()
        .map(alertRecordComposite -> tryToParseAlerts(
            alertRecordComposite, invalidAlerts, internalBatchId, options))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    log.info("Parsing done in: {}. Valid alerts: {}, Invalid alerts: {}", stopWatch,
        alerts.size(),
        invalidAlerts.size());

    return new AlertCompositeCollection(alerts, invalidAlerts);
  }

  private Optional<ValidAlertComposite> tryToParseAlerts(
      AlertRecordComposite readAlert,
      List<InvalidAlert> invalidAlerts,
      String internalBatchId,
      Option... options) {
    var alertId = readAlert.getAlertId();

    try {
      var alerts = alertMapper.fromAlertRecordComposite(readAlert, internalBatchId, options);
      return of(new ValidAlertComposite(alertId, alerts));
    } catch (Exception ex) {
      log.error("Error when parsing an alert: systemId={}", alertId.getSystemId(), ex);
      invalidAlerts.add(createDamagedAlert(alertId));
      return empty();
    }
  }

  private InvalidAlert createDamagedAlert(AlertId alertId) {
    return new InvalidAlert(alertId.getSystemId(), alertId.getBatchId(), Reason.DAMAGED);
  }

  private AlertRecordCompositeCollection getAlertCollection(
      List<AlertId> alertIds, ScbAlertIdContext context) {
    var stopWatch = StopWatch.createStarted();
    var timer = cbsOracleMetrics.alertRecordCompositeCollectionReaderTimer(
        context.getSourceView(), context.getHitDetailsView());
    var result =
        timer.record(() -> databaseAlertRecordCompositeReader.read(context, alertIds));
    log.info("AlertRecordCompositeCollection has been read from sourceView: {}, "
            + "cbsHitDetailsView: {}, alerts: {} executed in: {}", context.getSourceView(),
        context.getHitDetailsView(), alertIds.size(), stopWatch);
    return result;
  }

  private Option[] getMappingOptions(ScbAlertIdContext context) {
    var options = newArrayList(Option.FOR_RECOMMENDATION, Option.ATTACH_ALERT);

    if (context.getWatchlistLevel()) {
      options.add(Option.WATCHLIST_LEVEL);
    }

    if (processOnlyUnsolved) {
      options.add(Option.ONLY_UNSOLVED);
    }

    return options.toArray(Option[]::new);
  }
}
