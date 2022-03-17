package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper.Option;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
class AlertCompositeCollectionReader {

  private final AlertMapper alertMapper;
  private final DatabaseAlertRecordCompositeReader databaseAlertRecordCompositeReader;
  private final boolean processOnlyUnsolved;

  AlertCompositeCollection read(List<AlertId> alertIds, ScbAlertIdContext context) {
    var alertCollection = getAlertCollection(alertIds, context);
    var options = getMappingOptions(context);
    var invalidAlerts = new ArrayList<>(alertCollection.getInvalidAlerts());

    var alerts = alertCollection.getAlerts()
        .stream()
        .map(a -> tryToParseAlerts(a, invalidAlerts, options))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    return new AlertCompositeCollection(alerts, invalidAlerts);
  }

  private Optional<ValidAlertComposite> tryToParseAlerts(
      AlertRecordComposite readAlert, List<InvalidAlert> invalidAlerts, Option... options) {
    var alertId = readAlert.getAlertId();

    try {
      var alerts = alertMapper.fromAlertRecordComposite(readAlert, options);
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

    var sourceView = context.getSourceView();
    var cbsHitDetailsView = context.getHitDetailsView();

    if (cbsHitDetailsView.isEmpty()) {
      return databaseAlertRecordCompositeReader.read(sourceView, alertIds);
    }

    return databaseAlertRecordCompositeReader.readWithCbsHitDetails(
        sourceView, cbsHitDetailsView, alertIds);
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
