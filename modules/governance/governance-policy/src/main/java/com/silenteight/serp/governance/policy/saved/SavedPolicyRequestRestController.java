package com.silenteight.serp.governance.policy.saved;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
public class SavedPolicyRequestRestController {

  @NonNull
  private final SavedPolicyRequestQuery savedPolicyRequestQuery;

  @GetMapping(value = "/v1/policies", params = "state=SAVED")
  @PreAuthorize("isAuthorized('LIST_SAVED_POLICIES')")
  public ResponseEntity<Collection<PolicyDto>> saved(@RequestParam String state) {
    return ResponseEntity.ok(savedPolicyRequestQuery.listSaved());
  }
}
