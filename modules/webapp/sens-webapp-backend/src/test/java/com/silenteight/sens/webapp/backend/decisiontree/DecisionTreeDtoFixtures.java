package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecisionTreeDtoFixtures {

  public static final DecisionTreeDto INACTIVE = DecisionTreeDto
      .builder()
      .id(1L)
      .name("inactive-decision-tree")
      .status(StatusDtoFixtures.INACTIVE)
      .activations(emptyList())
      .build();

  public static final DecisionTreeDto ACTIVE = DecisionTreeDto
      .builder()
      .id(2L)
      .name("active-decision-tree")
      .status(StatusDtoFixtures.ACTIVE)
      .activations(asList("US_PERD_DENY", "HK_PRVB_DUDL"))
      .build();
}
