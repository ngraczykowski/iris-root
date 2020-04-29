package com.silenteight.sens.webapp.scb.chromeextension.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.scb.chromeextension.GetChromeExtensionConfigurationUseCase;
import com.silenteight.sens.webapp.scb.chromeextension.rest.dto.ChromeExtensionConfigurationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.INTERNAL;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ChromeExtensionRestController {

  @NonNull
  private final GetChromeExtensionConfigurationUseCase getConfigurationUseCase;

  @NonNull
  private final AuditLog auditLog;

  @GetMapping("/chrome-extension/config")
  public ResponseEntity<ChromeExtensionConfigurationDto> config() {
    auditLog.logInfo(INTERNAL, "Requesting Chrome Extension Configuration");

    ChromeExtensionConfigurationDto dto = getConfigurationUseCase.apply();

    auditLog.logInfo(INTERNAL, "Received Chrome Extension Configuration. dto={}", dto);

    return ok(dto);
  }
}
