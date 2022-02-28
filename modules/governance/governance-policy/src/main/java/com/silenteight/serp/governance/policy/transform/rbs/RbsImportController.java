package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class RbsImportController {

  private static final Duration IMPORT_TIMEOUT = Duration.ofSeconds(1);
  static final String IMPORT_FROM_RBS_URL = "/v1/policies/import:importFromRbs";

  @NonNull
  private final RbsImportUseCase rbsImportUseCase;

  @PostMapping(value = IMPORT_FROM_RBS_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("isAuthorized('IMPORT_POLICY_FROM_RBS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION,
          content = @Content)
  })
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
