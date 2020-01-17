package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;

import static com.silenteight.sens.webapp.backend.decisiontree.StatusDtoFixtures.ACTIVE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecisionTreeDetailsDtoFixtures {

  public static final DecisionTreeDetailsDto DEFAULT = DecisionTreeDetailsDto
      .builder()
      .id(1L)
      .name("default-decision-tree")
      .status(ACTIVE)
      .build();
}
