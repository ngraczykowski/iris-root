package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HitComposite {

  long hitId;

  String fkcoVMatchedTag;

  String fkcoISequence;

  String fkcoVListFmmId;

  public String getMatchId() {
    return fkcoVListFmmId + "(" + fkcoVMatchedTag + ", #" + fkcoISequence
        + ")";
  }
}
