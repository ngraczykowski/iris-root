package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class FeatureVectorStepRestController {

  @NonNull
  private final FindMatchingFeatureVectorsUseCase findMatchingFeatureVectorsUseCase;

  @GetMapping("/v1/steps/{id}/vectors")
  @PreAuthorize("isAuthorized('LIST_STEP_FEATURE_VECTORS')")
  public ResponseEntity<FeatureVectorsDto> featureVectors(@PathVariable UUID id) {
    return ok(findMatchingFeatureVectorsUseCase.activate(id));
  }
}
