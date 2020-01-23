package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;

import static com.silenteight.sens.webapp.backend.decisiontree.DecisionGroupDtoFixtures.HK_PRVB_DUDL;
import static com.silenteight.sens.webapp.backend.decisiontree.DecisionGroupDtoFixtures.US_PERD_DENY;
import static com.silenteight.sens.webapp.backend.decisiontree.StatusDtoFixtures.ACTIVE;
import static java.util.Arrays.asList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecisionTreeDetailsDtoFixtures {

  public static final DecisionTreeDetailsDto DEFAULT = DecisionTreeDetailsDto
      .builder()
      .id(1L)
      .name("default-decision-tree")
      .status(ACTIVE)
      .activations(asList(US_PERD_DENY, HK_PRVB_DUDL))
      .build();
}
