package com.silenteight.serp.governance.policy.importing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.importing.ImportPolicyController.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(RestConstants.ROOT)
@RequiredArgsConstructor
class ImportPolicyController {

  @NonNull
  private final ImportPolicyUseCase importPolicyUseCase;

  @PostMapping("/v1/policies/import")
  public ResponseEntity<Void> importPolicy(
      @RequestParam("file") MultipartFile file) throws IOException {

    ImportPolicyCommand command = ImportPolicyCommand.builder()
        .inputStream(file.getInputStream())
        .build();
    importPolicyUseCase.apply(command);

    return accepted().build();
  }

  static class RestConstants {

    public static final String ROOT = "/api";
  }
}
