package com.silenteight.sens.webapp.backend.configuration.auth;

import lombok.*;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sep.usermanagement.api.configuration.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({ ConfigurationAuthRestController.class })
class ConfigurationAuthRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ConfigurationQuery configurationQuery;

  @TestWithRole(roles = { USER_ADMINISTRATOR, AUDITOR, APPROVER, MODEL_TUNER })
  void its200WithCorrectBody_whenFound() {
    String authUrl = "https://auth.silenteight.com";
    String realm = "sens-webapp";
    String clientId = "frontend";
    given(configurationQuery.getAuthConfiguration())
        .willReturn(
            MockAuthConfigurationDto.builder()
                .url(authUrl)
                .realm(realm)
                .clientId(clientId)
                .build());

    get(mappingForAuthConfiguration())
        .statusCode(OK.value())
        .body("url", equalTo(authUrl))
        .body("realm", equalTo(realm))
        .body("clientId", equalTo(clientId));
  }

  private String mappingForAuthConfiguration() {
    return format("/configuration/auth");
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class MockAuthConfigurationDto extends AuthConfigurationDto {

    @NonNull
    private String url;
    @NonNull
    private String realm;
    @NonNull
    private String clientId;
  }
}
