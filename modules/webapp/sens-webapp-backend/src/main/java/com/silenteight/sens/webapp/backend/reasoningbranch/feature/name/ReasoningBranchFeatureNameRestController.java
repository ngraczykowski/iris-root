package com.silenteight.sens.webapp.backend.reasoningbranch.feature.name;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER_OR_BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchFeatureNameRestController {

  @NonNull
  private final FeatureNamesQuery featureNamesQuery;

  @GetMapping("/reasoning-branches/features/{featureVectorId}/names")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<List<String>> featureNames(
      @PathVariable long featureVectorId) {

    log.info(REASONING_BRANCH, "Listing Reasoning Branch Feature names. featureVectorId={}",
        featureVectorId);

    List<String> featureNames = featureNamesQuery.findFeatureNames(featureVectorId);

    log.info(REASONING_BRANCH, "Found {} Reasoning Branch Feature names", featureNames.size());

    return ok(featureNames);
  }
}
