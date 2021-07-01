package com.silenteight.serp.governance.model.transfer.export;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ExportModelRestController {

  public static final String EXPORT_MODEL_URL_PREFIX = "/v1/solvingModels/";
  public static final String EXPORT_MODEL_URL_SUFFIX = "/export";
  private static final String EXPORT_MODEL_URL = EXPORT_MODEL_URL_PREFIX + "{id}" +
      EXPORT_MODEL_URL_SUFFIX;
  @NonNull
  private final ExportModelUseCase exportModelUseCase;

  @GetMapping(EXPORT_MODEL_URL)
  @PreAuthorize("isAuthorized('EXPORT_MODEL')")
  public ResponseEntity<TransferredModelRootDto> export(@PathVariable UUID id) {
    return ok(exportModelUseCase.apply(id));
  }
}
