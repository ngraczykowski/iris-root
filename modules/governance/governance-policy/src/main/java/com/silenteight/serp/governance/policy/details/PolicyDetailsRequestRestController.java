package com.silenteight.serp.governance.policy.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.details.dto.PolicyDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PolicyDetailsRequestRestController {

  @NonNull
  private final PolicyDetailsUseCase policyDetailsUseCase;

  @GetMapping(value = "/v1/policies/{id}")
  @PreAuthorize("isAuthorized('LIST_POLICIES')")
  public ResponseEntity<PolicyDetailsDto> details(@PathVariable UUID id) {
    return ResponseEntity.ok(policyDetailsUseCase.activate(id));
  }
}
