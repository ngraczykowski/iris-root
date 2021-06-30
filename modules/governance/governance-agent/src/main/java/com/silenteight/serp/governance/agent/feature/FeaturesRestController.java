package com.silenteight.serp.governance.agent.feature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.domain.FeaturesProvider;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class FeaturesRestController {

  @NonNull
  private final FeaturesProvider featuresProvider;

  @GetMapping("/v1/features")
  @PreAuthorize("isAuthorized('LIST_FEATURES')")
  public ResponseEntity<FeaturesListDto> getFeaturesListDto() {
    return ResponseEntity.ok().body(featuresProvider.getFeaturesListDto());
  }
}
