package com.silenteight.serp.governance.agent.feature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.domain.FeaturesProvider;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.agent.domain.DomainConstants.AGENT_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = AGENT_ENDPOINT_TAG)
class FeaturesRestController {

  @NonNull
  private final FeaturesProvider featuresProvider;

  @GetMapping("/v1/features")
  @PreAuthorize("isAuthorized('LIST_FEATURES')")
  public ResponseEntity<FeaturesListDto> getFeaturesListDto() {
    return ResponseEntity.ok().body(featuresProvider.getFeaturesListDto());
  }
}
