package com.silenteight.simulator.management.details;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class SimulationDetailsRestController {

  @NonNull
  private final SimulationDetailsQuery simulationQuery;

  @GetMapping("/v1/simulations/{simulationId}")
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  public ResponseEntity<SimulationDetailsDto> get(@PathVariable UUID simulationId) {
    return ok(simulationQuery.get(simulationId));
  }
}
