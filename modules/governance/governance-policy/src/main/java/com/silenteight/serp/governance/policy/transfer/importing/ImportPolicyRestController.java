package com.silenteight.serp.governance.policy.transfer.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.common.PolicyImportedResponse;

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

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ImportPolicyRestController {

  @NonNull
  private final ImportPolicyUseCase importPolicyUseCase;

  @PostMapping(value = "/v1/policies/import", consumes = "multipart/form-data")
  @PreAuthorize("isAuthorized('IMPORT_POLICY')")
  public ResponseEntity<PolicyImportedResponse> importPolicy(
      @RequestParam("file") MultipartFile file, Authentication authentication) throws IOException {

    ImportPolicyCommand command = ImportPolicyCommand.builder()
                                                     .inputStream(file.getInputStream())
                                                     .createdBy(authentication.getName())
                                                     .build();
    UUID policyId = importPolicyUseCase.apply(command);

    return ok(new PolicyImportedResponse(policyId));
  }
}
