package com.silenteight.simulator.management.cancel;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.common.web.rest.RestConstants;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.NOT_FOUND_DESCRIPTION;
import static com.silenteight.simulator.common.web.rest.RestConstants.NOT_FOUND_STATUS;
import static com.silenteight.simulator.common.web.rest.RestConstants.NO_CONTENT_STATUS;
import static com.silenteight.simulator.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.simulator.management.domain.DomainConstants.SIMULATION_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
@Tag(name = SIMULATION_ENDPOINT_TAG)
class CancelSimulationRestController {

  @NonNull
  private final CancelSimulationUseCase useCase;

  @PostMapping("/v1/simulations/{id}:cancel")
  @PreAuthorize("isAuthorized('CANCEL_SIMULATION')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> cancel(@PathVariable UUID id, Authentication authentication) {
    log.info("Cancel simulation. simulationId={}.", id);

    CancelSimulationRequest request = CancelSimulationRequest.builder()
        .id(id)
        .canceledBy(authentication.getName())
        .build();
    useCase.activate(request);
    log.debug("Cancel simulation request processed.");

    return noContent().build();
  }
}
