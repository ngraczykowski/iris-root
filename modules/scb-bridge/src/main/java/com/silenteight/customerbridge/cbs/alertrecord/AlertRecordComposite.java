package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.customerbridge.cbs.alertid.AlertId;
import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;
import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.DecisionRecord;
import com.silenteight.proto.serp.v1.alert.Decision;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Builder
@Value
@Getter
public class AlertRecordComposite {

  @NonNull AlertRecord alert;
  @NonNull List<DecisionRecord> decisions;
  @NonNull List<CbsHitDetails> cbsHitDetails;

  public Optional<Instant> getLastResetDecisionDate() {
    return decisions
        .stream()
        .filter(DecisionRecord::isResetDecision)
        .map(DecisionRecord::getDecisionDate)
        .filter(Objects::nonNull)
        .max(Instant::compareTo);
  }

  public Optional<Decision> getLastDecision() {
    return decisions
        .stream()
        .filter(DecisionRecord::isAnalystDecision)
        .filter(d -> nonNull(d.getDecisionDate()))
        .max(Comparator.comparing(DecisionRecord::getDecisionDate))
        .map(DecisionRecord::toDecision);
  }

  public AlertId getAlertId() {
    return AlertId.builder().systemId(alert.getSystemId()).batchId(alert.getBatchId()).build();
  }

  public String getSystemId() {
    return alert.getSystemId();
  }

  public String getHitsDetails() {
    return alert.getDetails();
  }
}
