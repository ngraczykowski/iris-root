package com.silenteight.simulator.management.statistics;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.common.web.rest.RestConstants;
import com.silenteight.simulator.management.statistics.dto.EffectivenessDto;
import com.silenteight.simulator.management.statistics.dto.EfficiencyDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
class SimulationStatisticsRestController {

  static final String EFFICIENCY_URL = "/v1//simulations/{id}/statistics/efficiency";
  static final String EFFECTIVENESS_URL = "/v1/simulations/{id}/statistics/effectiveness";

  @NonNull
  private final StaticSimulationStatisticsService simulationStatisticsService;

  @GetMapping(value = EFFICIENCY_URL)
  @PreAuthorize("isAuthorized('SIMULATION_STATISTICS')")
  public ResponseEntity<EfficiencyDto> getEfficiency(@PathVariable UUID id) {
    return ok(simulationStatisticsService.getEfficiency(id));
  }

  @GetMapping(value = EFFECTIVENESS_URL)
  @PreAuthorize("isAuthorized('SIMULATION_STATISTICS')")
  public ResponseEntity<EffectivenessDto> getEffectiveness(@PathVariable UUID id) {
    return ok(simulationStatisticsService.getEffectiveness(id));
  }
}
