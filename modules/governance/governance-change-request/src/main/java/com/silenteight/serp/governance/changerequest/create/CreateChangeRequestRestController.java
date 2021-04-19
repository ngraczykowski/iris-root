package com.silenteight.serp.governance.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.create.dto.CreateChangeRequestDto;

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
class CreateChangeRequestRestController {

  @NonNull
  private final CreateChangeRequestUseCase createChangeRequestUseCase;

  @PostMapping("/v1/changeRequests")
  @PreAuthorize("isAuthorized('CREATE_CHANGE_REQUEST')")
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateChangeRequestDto dto, Authentication authentication) {

    CreateChangeRequestCommand command = CreateChangeRequestCommand.builder()
        .id(dto.getId())
        .modelName(dto.getModelName())
        .createdBy(authentication.getName())
        .creatorComment(dto.getComment())
        .build();
    createChangeRequestUseCase.activate(command);
    return accepted().build();
  }
}
