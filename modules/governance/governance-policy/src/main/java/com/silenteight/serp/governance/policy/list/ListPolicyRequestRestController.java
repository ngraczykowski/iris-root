package com.silenteight.serp.governance.policy.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static java.util.List.of;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
public class ListPolicyRequestRestController {

  @NonNull
  private final ListPoliciesRequestQuery listPolicyRequestQuery;

  @GetMapping("/v1/policies")
  @PreAuthorize("isAuthorized('LIST_POLICIES')")
  public ResponseEntity<Collection<PolicyDto>> list() {
    log.info("Listing all policies.");
    return ResponseEntity.ok(listPolicyRequestQuery.listAll());
  }

  @GetMapping(value = "/v1/policies", params = "state")
  @PreAuthorize("isAuthorized('LIST_POLICIES')")
  public ResponseEntity<Collection<PolicyDto>> list(@RequestParam PolicyState... state) {
    log.info("Listing all policies in states={}", of(state));
    return ResponseEntity.ok(listPolicyRequestQuery.list(of(state)));
  }
}
