package com.silenteight.serp.governance.policy.transfer.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.common.PolicyImportedResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class ImportPolicyRestController {

  @NonNull
  private final ImportPolicyUseCase importPolicyUseCase;

  @PostMapping(value = "/v1/policies/import", consumes = "multipart/form-data")
  @PreAuthorize("isAuthorized('IMPORT_POLICY')")
  public ResponseEntity<PolicyImportedResponse> importPolicy(
      @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {

    log.info("Importing policy from file. fileName={}", file.getOriginalFilename());

    ImportPolicyCommand command = ImportPolicyCommand.builder()
        .inputStream(file.getInputStream())
        .createdBy(authentication.getName())
        .build();
    UUID policyId = importPolicyUseCase.apply(command);

    log.debug("Importing policy from file request processed.");
    return ok(new PolicyImportedResponse(policyId));
  }
}
