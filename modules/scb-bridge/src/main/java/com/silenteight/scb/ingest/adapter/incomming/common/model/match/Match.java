package com.silenteight.scb.ingest.adapter.incomming.common.model.match;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;

public record Match(
    ObjectId id,
    MatchedParty matchedParty,
    int index,
    int flags,
    MatchDetails details) {

  @Builder
  public Match {}

  public boolean isNew() {
    return !isObsolete() && !isSolved();
  }

  public boolean isObsolete() {
    return 0 != (flags & Flag.OBSOLETE.value);
  }

  public boolean isSolved() {
    return 0 != (flags & Flag.SOLVED.value);
  }

  public enum Flag {
    NONE(0),
    OBSOLETE(1),
    SOLVED(2);

    @Getter
    private final int value;

    Flag(int value) {
      this.value = value;
    }
  }
}
