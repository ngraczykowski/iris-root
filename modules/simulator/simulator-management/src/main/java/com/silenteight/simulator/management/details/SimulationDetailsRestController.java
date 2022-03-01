package com.silenteight.simulator.management.details;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.*;
import static com.silenteight.simulator.management.domain.DomainConstants.SIMULATION_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = SIMULATION_ENDPOINT_TAG)
class SimulationDetailsRestController {

  @NonNull
  private final SimulationDetailsQuery simulationQuery;

  @GetMapping("/v1/simulations/{simulationId}")
  @PreAuthorize("isAuthorized('VIEW_SIMULATION')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION,
          content = @Content())
  })
  public ResponseEntity<SimulationDetailsDto> get(@PathVariable UUID simulationId) {
    return ok(simulationQuery.get(simulationId));
  }
}
