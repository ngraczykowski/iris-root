package com.silenteight.serp.governance.bulkchange;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Embeddable
class ReasoningBranchIdToChange {

  private long decisionTreeId;
  private long featureVectorId;

  ReasoningBranchIdToApply toApply() {
    return new ReasoningBranchIdToApply(decisionTreeId, featureVectorId);
  }
}

