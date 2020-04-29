package com.silenteight.sens.webapp.scb.chromeextension.rest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class GnsUrlPatternDto {

  @NonNull
  private final String openRecord;
  @NonNull
  private final String solution;
  @NonNull
  private final String hits;
}
