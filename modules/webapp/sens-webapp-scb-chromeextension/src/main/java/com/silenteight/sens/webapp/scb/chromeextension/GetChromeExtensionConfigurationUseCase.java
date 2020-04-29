package com.silenteight.sens.webapp.scb.chromeextension;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.scb.chromeextension.rest.dto.ChromeExtensionConfigurationDto;
import com.silenteight.sens.webapp.scb.chromeextension.rest.dto.GnsUrlPatternDto;

@RequiredArgsConstructor
public class GetChromeExtensionConfigurationUseCase {

  private final ChromeExtensionProperties properties;

  public ChromeExtensionConfigurationDto apply() {
    return ChromeExtensionConfigurationDto
        .builder()
        .authUrl(properties.getAuthUrl())
        .recommendationUrl(properties.getRecommendationUrl())
        .gnsUrlPattern(createGnsUrlPattern())
        .sensLogLevel(properties.getLogLevel())
        .commentLengthThreshold(properties.getCommentLengthThreshold())
        .refreshIntervalInMs(properties.getRefreshIntervalInMs())
        .build();
  }

  private GnsUrlPatternDto createGnsUrlPattern() {
    return GnsUrlPatternDto
        .builder()
        .openRecord(properties.getOpenRecordUrlPattern())
        .solution(properties.getSolutionUrlPattern())
        .hits(properties.getHitsUrlPattern())
        .build();
  }
}
