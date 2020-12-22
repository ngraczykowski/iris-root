package com.silenteight.serp.governance.bulkchange;

import lombok.Value;

import java.io.Serializable;

@Value
public class ReasoningBranchIdToApply implements Serializable {

  private static final long serialVersionUID = 7252803341360902997L;

  long decisionTreeId;
  long featureVectorId;
}
