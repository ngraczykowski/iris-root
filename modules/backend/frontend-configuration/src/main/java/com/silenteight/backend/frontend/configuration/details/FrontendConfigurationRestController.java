package com.silenteight.backend.frontend.configuration.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.silenteight.sep.auth.authentication.RestConstants.OK_STATUS;
import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.sep.auth.authentication.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.serp.governance.common.web.rest.OpenAPITags.CONFIGURATION_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = CONFIGURATION_ENDPOINT_TAG)
class FrontendConfigurationRestController {

  static final String FRONTEND_CONFIGURATION_URL = "/v1/frontend/configuration";

  @NonNull
  private final FrontendConfigurationProperties frontendConfigurationProperties;

  @GetMapping(FRONTEND_CONFIGURATION_URL)
  @ApiResponses(
      value = @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION))
  public ResponseEntity<Map<String, String>> getFrontendConfiguration() {
    return ok(frontendConfigurationProperties.getConfiguration());
  }
}
