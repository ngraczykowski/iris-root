package com.silenteight.serp.governance.model.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.create.dto.CreateModelDto;

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

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreateModelRestController {

  @NonNull
  private final CreateModelUseCase createModelUseCase;

  @PostMapping("/v1/models")
  @PreAuthorize("isAuthorized('CREATE_MODEL')")
  public ResponseEntity<Void> create(
      @Valid @RequestBody CreateModelDto request, Authentication authentication) {

    CreateModelCommand command = CreateModelCommand.builder()
        .id(request.getId())
        .policyName(request.getPolicyName())
        .createdBy(authentication.getName())
        .build();
    createModelUseCase.activate(command);
    return accepted().build();
  }
}
