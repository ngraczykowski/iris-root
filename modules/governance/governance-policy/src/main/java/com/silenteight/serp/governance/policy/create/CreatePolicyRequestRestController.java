package com.silenteight.serp.governance.policy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.create.dto.CreatePolicyDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreatePolicyRequestRestController {

  @NonNull
  private final CreatePolicyUseCase createPolicyUseCase;

  @PostMapping("/v1/policies")
  @PreAuthorize("isAuthorized('CREATE_POLICY')")
  public ResponseEntity<Void> create(
      @Valid @RequestBody CreatePolicyDto createPolicyDto, Authentication authentication) {

    CreatePolicyCommand command = CreatePolicyCommand
        .builder()
        .id(createPolicyDto.getId())
        .policyName(createPolicyDto.getPolicyName())
        .createdBy(authentication.getName())
        .build();
    createPolicyUseCase.activate(command);
    return accepted().build();
  }
}
