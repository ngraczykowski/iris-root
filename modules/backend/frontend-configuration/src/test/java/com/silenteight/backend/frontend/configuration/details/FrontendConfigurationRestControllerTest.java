/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.frontend.configuration.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import static com.silenteight.backend.frontend.configuration.details.FrontendConfigurationRestController.FRONTEND_CONFIGURATION_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.OK;

@Import(FrontendConfigurationRestController.class)
@EnableConfigurationProperties(FrontendConfigurationProperties.class)
class FrontendConfigurationRestControllerTest extends BaseRestControllerTest {

  @Test
  void return200WithoutLogin() {
    get(FRONTEND_CONFIGURATION_URL)
        .statusCode(OK.value())
        .body("serverApiUrl", is("/rest/governance/api/v1"))
        .body("governanceApiUrl", is("/rest/governance/api/v1"))
        .body("simulatorUrl", is("/rest/governance/api/v1"));
  }
}
