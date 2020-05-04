package com.silenteight.sens.webapp.scb.chrome.extension.rest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ChromeExtensionConfigurationDto {

  @NonNull
  private final String authUrl;
  @NonNull
  private final String recommendationUrl;
  @NonNull
  private final GnsUrlPatternDto gnsUrlPattern;
  @NonNull
  private final String sensLogLevel;
  private final int commentLengthThreshold;
  private final int refreshIntervalInMs;
}
