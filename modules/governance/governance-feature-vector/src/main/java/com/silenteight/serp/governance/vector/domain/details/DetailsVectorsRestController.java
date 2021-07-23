package com.silenteight.serp.governance.vector.domain.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DetailsVectorsRestController {

  @NonNull
  private final VectorDetailQuery vectorDetailQuery;

  @GetMapping(value = "/v1/vectors/details")
  @PreAuthorize("isAuthorized('LIST_VECTORS')")
  public ResponseEntity<FeatureVectorWithUsageDto> getWithUsage(
      @RequestParam("fvSignature") String fvSignature) {

    log.debug("Getting FV with usage for signature: {}", fvSignature);
    return ok().body(vectorDetailQuery.findByFvSignature(fvSignature));
  }
}
