package com.silenteight.sens.webapp.backend.chromeextension.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.chromeextension.GetChromeExtensionConfigurationUseCase;
import com.silenteight.sens.webapp.backend.chromeextension.rest.dto.ChromeExtensionConfigurationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ChromeExtensionRestController {

  @NonNull
  private final GetChromeExtensionConfigurationUseCase getConfigurationUseCase;

  @GetMapping("/chrome-extension/config")
  public ResponseEntity<ChromeExtensionConfigurationDto> config() {
    log.info(INTERNAL, "Requesting Chrome Extension Configuration");

    ChromeExtensionConfigurationDto dto = getConfigurationUseCase.apply();

    log.info(INTERNAL, "Received Chrome Extension Configuration. dto={}", dto);

    return ok(dto);
  }
}
