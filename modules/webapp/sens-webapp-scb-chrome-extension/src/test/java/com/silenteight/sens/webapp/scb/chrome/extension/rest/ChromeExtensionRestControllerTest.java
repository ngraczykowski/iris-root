package com.silenteight.sens.webapp.scb.chrome.extension.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.scb.chrome.extension.GetChromeExtensionConfigurationUseCase;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.sens.webapp.scb.chrome.extension.rest.ChromeExtensionRestControllerFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({ ChromeExtensionRestController.class })
class ChromeExtensionRestControllerTest extends BaseRestControllerTest {

  private static final String GET_CONFIGURATION_PATH = "/chrome-extension/config";

  @MockBean
  private GetChromeExtensionConfigurationUseCase getConfigurationUseCase;

  @TestWithRole(roles = { USER_ADMINISTRATOR, APPROVER, AUDITOR, MODEL_TUNER })
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
