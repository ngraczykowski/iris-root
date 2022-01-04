package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class HitComposite {

  long hitId;

  String fkcoVMatchedTag;

  String fkcoISequence;

  String fkcoVListFmmId;

  String fkcoVListCity;

  String fkcoVListState;

  String fkcoVListCountry;

  public String getMatchId() {
    return fkcoVListFmmId + "(" + fkcoVMatchedTag + ", #" + fkcoISequence
        + ")";
  }

  public String getWatchlistLocation() {
    return String.join(", ", List.of(fkcoVListCountry, fkcoVListState, fkcoVListCity));
  }
}
