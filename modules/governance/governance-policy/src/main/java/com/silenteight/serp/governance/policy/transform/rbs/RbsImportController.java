package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.ForkJoinPool;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class RbsImportController {

  private static final Duration IMPORT_TIMEOUT = Duration.ofSeconds(1);
  static final String IMPORT_FROM_RBS_URL = "/v1/policies/import:importFromRbs";

  @NonNull
  private final RbsImportUseCase rbsImportUseCase;

  @PostMapping(value = IMPORT_FROM_RBS_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthorized('IMPORT_POLICY_FROM_RBS')")
  public DeferredResult<ResponseEntity<?>> importPolicy(@RequestParam("file") MultipartFile file) {
    String filename = file.getOriginalFilename();
    requireNonNull(filename);

    DeferredResult<ResponseEntity<?>> output =
        new DeferredResult<>(IMPORT_TIMEOUT.toMillis(), accepted().build());

    ForkJoinPool.commonPool().submit(() -> {
      try (InputStream importInputStream = file.getInputStream()) {
        importFromInputStream(importInputStream, filename);
        output.setResult(accepted().build());
      } catch (Exception e) {
        output.setErrorResult(e);
      }
    });

    return output;
  }

  private void importFromInputStream(InputStream importInputStream, String filename) {
    RbsImportCommand rbsImportCommand = RbsImportCommand.builder()
        .inputStream(importInputStream)
        .fileName(filename)
        .build();

    rbsImportUseCase.apply(rbsImportCommand);
  }
}
