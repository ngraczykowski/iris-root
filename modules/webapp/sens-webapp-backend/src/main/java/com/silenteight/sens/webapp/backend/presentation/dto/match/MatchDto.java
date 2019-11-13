package com.silenteight.sens.webapp.backend.presentation.dto.match;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import javax.annotation.Nullable;

@Data
public class MatchDto {

  private final long id;
  @NonNull
  private final String externalId;
  @Nullable
  private final String discriminator;
  @NonNull
  private final List<Object> matchFields;
  private final long matchGroupId;
}
