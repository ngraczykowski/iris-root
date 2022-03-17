package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Slf4j
@Builder
public class RecordCompositeWriter implements ItemWriter<AlertComposite> {

  private final GnsSyncDeltaService deltaService;
  private final BatchAlertIngestService ingestService;
  private final CbsAckGateway cbsAckGateway;
  private final WriterConfiguration configuration;
  private final String deltaJobName;

  @Override
  public void write(List<? extends AlertComposite> items) {
    ingestAlerts(items);

    if (configuration.isUseDelta())
      deltaService.updateDelta(getDeltas(items), deltaJobName);

    if (log.isDebugEnabled())
      log.debug("Saved records: count={}", items.size());

    //TODO: to remove, the acknowledgment is not used while learning
    if (configuration.isAckRecords())
      ackReadAlerts(items);
  }

  private void ingestAlerts(List<? extends AlertComposite> items) {
    Stream<Alert> alerts = items.stream().map(AlertComposite::getAlert);
    if (configuration.isLearningMode())
      ingestService.ingestAlertsForLearn(alerts);
    else
      ingestService.ingestAlertsForRecommendation(alerts);
  }

  private static Map<String, Integer> getDeltas(List<? extends AlertComposite> items) {
    var alertCollector = toMap(
        AlertComposite::getSystemId, AlertComposite::getDecisionsCount,
        (existing, replacement) -> existing);

    return items
        .stream()
        .collect(alertCollector);
  }

  private void ackReadAlerts(List<? extends AlertComposite> items) {
    Set<CbsAckAlert> readAlerts = items
        .stream()
        .map(alertComposite -> alertComposite.getAlert().details())
        .map(AlertCompositeToAckAlertMapper::toAckAlert)
        .collect(toSet());

    cbsAckGateway.ackReadAlerts(readAlerts);
  }

  @Getter
  @Value(staticConstructor = "of")
  static class WriterConfiguration {

    private final boolean useDelta;
    private final boolean ackRecords;
    private final boolean learningMode;
  }
}
