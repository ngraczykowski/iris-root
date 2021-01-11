package com.silenteight.serp.governance.policy.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.serp.governance.policy.importing.ImportPolicyController.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(RestConstants.ROOT)
@RequiredArgsConstructor
class ImportPolicyController {

  @NonNull
  private final ImportPolicyUseCase importPolicyUseCase;

  @PostMapping(value = "/v1/policies/import", consumes = "multipart/form-data")
  @PreAuthorize("isAuthorized('IMPORT_POLICY')")
  public ResponseEntity<PolicyImportedResponse> importPolicy(
      @RequestParam("file") MultipartFile file) throws IOException {

    ImportPolicyCommand command = ImportPolicyCommand
        .builder().inputStream(file.getInputStream()).build();
    UUID policyId = importPolicyUseCase.apply(command);

    return ok(new PolicyImportedResponse(policyId));
  }

  @Value
  static class PolicyImportedResponse {
    UUID policyId;
  }

  static class RestConstants {

    public static final String ROOT = "/api";
  }
}
