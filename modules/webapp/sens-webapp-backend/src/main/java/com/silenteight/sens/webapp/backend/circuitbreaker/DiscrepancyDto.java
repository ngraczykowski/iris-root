package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
@Builder
public class DiscrepancyDto {

  private long id;
  @NonNull
  private String alertId;
  @NonNull
  private String aiComment;
  @NonNull
  private Instant aiCommentDate;
  @NonNull
  private String analystComment;
  @NonNull
  private Instant analystCommentDate;
}
