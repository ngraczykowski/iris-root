package com.silenteight.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;

import java.time.Instant;
import java.util.Collection;
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
    Collection<Decision> decisions) {

  @Builder(toBuilder = true)
  public Alert {}

  public int getMatchesCount() {
    return matches != null ? matches.size() : 0;
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