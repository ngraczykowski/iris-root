package com.silenteight.serp.governance.vector.domain.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.vector.domain.DomainConstants.VECTOR_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = VECTOR_ENDPOINT_TAG)
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
