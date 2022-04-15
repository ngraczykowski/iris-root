package com.silenteight.sens.webapp.scb.chrome.extension.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.scb.chrome.extension.GetChromeExtensionConfigurationUseCase;
import com.silenteight.sens.webapp.scb.chrome.extension.rest.dto.ChromeExtensionConfigurationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
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
    log.info("Requesting Chrome Extension Configuration");

    ChromeExtensionConfigurationDto dto = getConfigurationUseCase.apply();

    log.info("Received Chrome Extension Configuration. dto={}", dto);

    return ok(dto);
  }
}
