package com.silenteight.serp.governance.policy.domain.events;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEvent;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class NewPolicyInUseEvent extends BaseEvent {

  private static final long serialVersionUID = 5429401906070764168L;

  @NonNull
  private final UUID policyId;
  @NonNull
  private final UUID correlationId;
}
