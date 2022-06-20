/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

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
    return new AlertId(alert.getSystemId(), alert.getBatchId());
  }

  public String getSystemId() {
    return alert.getSystemId();
  }

  public String getHitsDetails() {
    return alert.getDetails();
  }
}
