package com.silenteight.simulator.management.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.common.web.rest.RestConstants;
import com.silenteight.simulator.management.list.dto.SimulationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
class ListSimulationRestController {

  static final String SIMULATIONS_URL = "/v1/simulations";

  @NonNull
  private final ListSimulationsQuery simulationQuery;

  @GetMapping(value = SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  public ResponseEntity<List<SimulationDto>> list() {
    return ok(simulationQuery.list());
  }

  @GetMapping(value = SIMULATIONS_URL, params = "model")
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  public ResponseEntity<List<SimulationDto>> listForModel(@RequestParam String model) {
    return ok(simulationQuery.findByModel(model));
  }
}
