package com.silenteight.serp.governance.policy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.create.dto.CreatePolicyDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class CreatePolicyRequestRestController {

  @NonNull
  private final CreatePolicyUseCase createPolicyUseCase;

  @PostMapping("/v1/policies")
  @PreAuthorize("isAuthorized('CREATE_POLICY')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> create(
      @Valid @RequestBody CreatePolicyDto createPolicyDto, Authentication authentication) {

    CreatePolicyCommand command = CreatePolicyCommand
        .builder()
        .id(createPolicyDto.getId())
        .policyName(createPolicyDto.getPolicyName())
        .createdBy(authentication.getName())
        .build();
    createPolicyUseCase.activate(command);
    //TODO https://silent8.atlassian.net/browse/WEB-2231
    return accepted().build();
  }
}
