package com.silenteight.serp.governance.model.archive;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.model.api.v1.ModelsArchived;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ModelsArchivedFixtures {

  private static final String MODEL_NAME_1 = "solvingModels/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String MODEL_NAME_2 = "solvingModels/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final Set<String> MODEL_NAMES = Set.of(MODEL_NAME_1, MODEL_NAME_2);

  static final ModelsArchived MODELS_ARCHIVED_MESSAGE =
      ModelsArchived.newBuilder()
          .addAllModels(MODEL_NAMES)
          .build();
}
