package com.silenteight.serp.governance.decisiongroup;

import lombok.EqualsAndHashCode;
import lombok.Value;

import com.silenteight.sep.base.common.entity.BaseEvent;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class DecisionGroupCreated extends BaseEvent {

  private static final long serialVersionUID = 9219630517110599012L;

  UUID uuid = UUID.randomUUID();

  @EqualsAndHashCode.Include
  long decisionGroupId;
}
