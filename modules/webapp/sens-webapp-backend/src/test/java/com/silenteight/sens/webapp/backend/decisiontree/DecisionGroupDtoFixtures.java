package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionGroupDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecisionGroupDtoFixtures {

  public static final DecisionGroupDto US_PERD_DENY = DecisionGroupDto
      .builder()
      .name("US_PERD_DENY")
      .build();

  public static final DecisionGroupDto HK_PRVB_DUDL = DecisionGroupDto
      .builder()
      .name("HK_PRVB_DUDL")
      .build();
}
