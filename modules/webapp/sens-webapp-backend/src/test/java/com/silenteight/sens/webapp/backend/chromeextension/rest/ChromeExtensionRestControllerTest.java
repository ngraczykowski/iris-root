package com.silenteight.sens.webapp.backend.chromeextension.rest;

import com.silenteight.sens.webapp.backend.chromeextension.GetChromeExtensionConfigurationUseCase;
import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.chromeextension.rest.ChromeExtensionRestControllerFixtures.*;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({ ChromeExtensionRestController.class })
class ChromeExtensionRestControllerTest extends BaseRestControllerTest {

  private static final String GET_CONFIGURATION_PATH = "/chrome-extension/config";

  @MockBean
  private GetChromeExtensionConfigurationUseCase getConfigurationUseCase;

  @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenAvailable() {
    given(getConfigurationUseCase.apply()).willReturn(CHROME_EXTENSION_CONFIGURATION);

    get(GET_CONFIGURATION_PATH)
        .statusCode(OK.value())
        .body("authUrl", equalTo(AUTH_URL))
        .body("recommendationUrl", equalTo(RECOMMENDATION_URL))
        .body("gnsUrlPattern.openRecord", equalTo(OPEN_RECORD))
        .body("gnsUrlPattern.solution", equalTo(SOLUTION))
        .body("gnsUrlPattern.hits", equalTo(HITS))
        .body("sensLogLevel", equalTo(SENS_LOG_LEVEL))
        .body("commentLengthThreshold", equalTo(COMMENT_LENGTH_THRESHOLD))
        .body("refreshIntervalInMs", equalTo(REFRESH_INTERVAL_IN_MS));
  }
}
