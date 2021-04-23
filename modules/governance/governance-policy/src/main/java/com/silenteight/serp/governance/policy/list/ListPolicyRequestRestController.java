package com.silenteight.serp.governance.policy.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.util.List.of;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
public class ListPolicyRequestRestController {

  @NonNull
  private final ListPoliciesRequestQuery listPolicyRequestQuery;

  @GetMapping("/v1/policies")
  @PreAuthorize("isAuthorized('LIST_POLICIES')")
  public ResponseEntity<Collection<PolicyDto>> list() {
    return ResponseEntity.ok(listPolicyRequestQuery.listAll());
  }

  @GetMapping(value = "/v1/policies", params = "state")
  @PreAuthorize("isAuthorized('LIST_POLICIES')")
  public ResponseEntity<Collection<PolicyDto>> list(@RequestParam PolicyState... state) {
    return ResponseEntity.ok(listPolicyRequestQuery.list(of(state)));
  }
}
