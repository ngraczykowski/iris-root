package com.silenteight.sens.webapp.backend.configuration.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.configuration.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ConfigurationAuthRestController {

  @NonNull
  private final ConfigurationQuery configurationQuery;

  @GetMapping("/configuration/auth")
  public ResponseEntity<AuthConfigurationDto> getAuthConfiguration() {
    log.info(INTERNAL, "Get Auth Configuration");

    AuthConfigurationDto configuration = configurationQuery.getAuthConfiguration();

    log.info(INTERNAL, "Found Auth Configuration. configuration={}", configuration);

    return ok(configuration);
  }
}
