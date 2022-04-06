package com.silenteight.serp.governance.model.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.model.domain.DomainConstants.SOLVING_MODEL_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = SOLVING_MODEL_ENDPOINT_TAG)
class GetModelRestController {

  @NonNull
  private final ModelDetailsQuery getModelDetailsQuery;

  @GetMapping("/v1/solvingModels/{id}")
  @PreAuthorize("isAuthorized('LIST_MODELS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION,
          content = @Content)
  })
  public ResponseEntity<ModelDto> get(@PathVariable UUID id) {
    log.info("Getting model details for modelId={}", id);
    return ok(getModelDetailsQuery.get(id));
  }

  @GetMapping(value = "/v1/solvingModels", params = "policy")
  @PreAuthorize("isAuthorized('LIST_MODELS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION,
          content = @Content)
  })
  public ResponseEntity<List<ModelDto>> getByPolicy(@RequestParam String policy) {
    log.info("Listing models.");
    return ok(getModelDetailsQuery.getByPolicy(policy));
  }
}
