package com.silenteight.serp.governance.activation.dto;

import lombok.Value;

import java.util.Collection;
import java.util.Collections;

@Value(staticConstructor = "of")
public class ActivationRequest {

  long decisionTreeId;
  Collection<Long> decisionGroupIds;

  public static ActivationRequest of(long treeId, long groupId) {
    return new ActivationRequest(treeId, Collections.singleton(groupId));
  }
}
