package com.silenteight.serp.governance.model.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.create.dto.CreateModelDto;

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
import static com.silenteight.serp.governance.model.domain.DomainConstants.SOLVING_MODEL_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = SOLVING_MODEL_ENDPOINT_TAG)
class CreateModelRestController {

  @NonNull
  private final CreateModelUseCase createModelUseCase;

  @PostMapping("/v1/solvingModels")
  @PreAuthorize("isAuthorized('CREATE_MODEL')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION),
      @ApiResponse(responseCode = CONFLICT_STATUS, description = "model already exists")
  })
  public ResponseEntity<Void> create(
      @Valid @RequestBody CreateModelDto request, Authentication authentication) {
    log.info("Creating model.CreateModelDto={}", request);
    CreateModelCommand command = CreateModelCommand.builder()
        .id(request.getId())
        .policy(request.getPolicy())
        .createdBy(authentication.getName())
        .build();
    createModelUseCase.activate(command);
    log.debug("Create model request processed.");
    return accepted().build();
  }
}
