package com.silenteight.serp.governance.policy.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.silenteight.sep.base.common.entity.BaseEvent;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class PolicyPromotedEvent extends BaseEvent {

  private static final long serialVersionUID = 5429401906070764168L;

  private final UUID policyId;
}
