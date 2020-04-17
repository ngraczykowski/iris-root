package com.silenteight.sens.webapp.backend.chromeextension;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Validated
public class ChromeExtensionProperties {

  private String authUrl;
  private String recommendationUrl;
  private String openRecordUrlPattern;
  private String solutionUrlPattern;
  private String hitsUrlPattern;
  @NotBlank
  private String logLevel;
  @Min(1)
  private int commentLengthThreshold;
  @Min(1)
  private int refreshIntervalInMs;
}
