package com.silenteight.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;

import java.time.Instant;
import java.util.List;

public record Alert(
    ObjectId id,
    String decisionGroup,
    String securityGroup,
    int flags,
    State state,
    Instant generatedAt,
    Instant receivedAt,
    Instant ingestedAt,
    Instant processedAt,
    AlertedParty alertedParty,
    List<Match> matches,
    AlertDetails details,
    List<Decision> decisions) {

  public String logInfo() {
    return String.format(
        "Alert(alertId: %s, discriminator: %s, numOfMatches: %d, numOfDecisions: %d, batchId: %s, "
            + "internalBatchId: %s)",
        id.sourceId(),
        id.discriminator(),
        getMatchesCount(),
        getDecisionsCount(),
        details.getBatchId(),
        details.getInternalBatchId());
  }

  @Builder(toBuilder = true)
  public Alert {}

  public int getDecisionsCount() {
    return decisions != null ? decisions.size() : 0;
  }

  public boolean hasDecision() {
    return getDecisionsCount() > 0;
  }

  public int getMatchesCount() {
    return matches != null ? matches.size() : 0;
  }

  public boolean isLearnFlag() {
    return (flags & Flag.LEARN.getValue()) != 0;
  }

  public String getName() {
    return details.getAlertName();
  }

  public enum State {
    STATE_CORRECT,
    STATE_INVALID,
    STATE_DAMAGED,
    UNRECOGNIZED
  }

  public enum Flag {
    NONE(0),
    RECOMMEND(1),
    LEARN(2),
    PROCESS(4),
    ATTACH(8),
    UNRECOGNIZED(-1);

    @Getter
    private final int value;

    Flag(int value) {
      this.value = value;
    }
  }
}