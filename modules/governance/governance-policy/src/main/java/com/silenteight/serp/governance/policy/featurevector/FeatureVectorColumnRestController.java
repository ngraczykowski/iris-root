package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsColumnsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class FeatureVectorColumnRestController {

  @NonNull
  private final FindFeatureVectorsColumnsUseCase findFeatureVectorsColumnsUseCase;

  @GetMapping(value = "/v1/steps/vectors/columns")
  @PreAuthorize("isAuthorized('LIST_STEP_FEATURE_VECTORS')")
  public ResponseEntity<FeatureVectorsColumnsDto> featureVectorsColumns() {
    return ok(findFeatureVectorsColumnsUseCase.activate());
  }
}
