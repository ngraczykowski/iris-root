package com.silenteight.sens.webapp.scb.chrome.extension.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.scb.chrome.extension.rest.dto.ChromeExtensionConfigurationDto;
import com.silenteight.sens.webapp.scb.chrome.extension.rest.dto.GnsUrlPatternDto;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ChromeExtensionRestControllerFixtures {

  static final String AUTH_URL = "http://localhost:8080/auth";
  static final String RECOMMENDATION_URL = "http://localhost:8080/recommendation";
  static final String OPEN_RECORD = "http://localhost:8080/openRecord";
  static final String SOLUTION = "http://localhost:8080/solution";
  static final String HITS = "http://localhost:8080/hits";
  static final String SENS_LOG_LEVEL = "debug";
  static final int COMMENT_LENGTH_THRESHOLD = 100;
  static final int REFRESH_INTERVAL_IN_MS = 1000;

  private static final GnsUrlPatternDto GNS_URL_PATTERN =
      GnsUrlPatternDto
          .builder()
          .openRecord(OPEN_RECORD)
          .solution(SOLUTION)
          .hits(HITS)
          .build();

  static final ChromeExtensionConfigurationDto CHROME_EXTENSION_CONFIGURATION =
      ChromeExtensionConfigurationDto
          .builder()
          .authUrl(AUTH_URL)
          .recommendationUrl(RECOMMENDATION_URL)
          .gnsUrlPattern(GNS_URL_PATTERN)
          .sensLogLevel(SENS_LOG_LEVEL)
          .commentLengthThreshold(COMMENT_LENGTH_THRESHOLD)
          .refreshIntervalInMs(REFRESH_INTERVAL_IN_MS)
          .build();
}
