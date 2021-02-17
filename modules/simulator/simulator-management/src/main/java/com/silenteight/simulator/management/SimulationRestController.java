package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.common.web.rest.RestConstants;
import com.silenteight.simulator.management.dto.CreateSimulationRequest;
import com.silenteight.simulator.management.dto.SimulationDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
public class SimulationRestController {

  static final String SIMULATIONS_URL = "/v1/simulations";

  @NonNull
  private final CreateSimulationUseCase useCase;

  @NonNull
  private final SimulationQuery simulationQuery;

  @PostMapping(SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION')")
  public ResponseEntity<Void> createDataset(
      @RequestBody @Valid CreateSimulationRequest createSimulationRequest,
      Authentication authentication) {

    useCase.activate(createSimulationRequest, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  public ResponseEntity<List<SimulationDto>> listSimulations() {
    return ResponseEntity.ok(simulationQuery.listAllSimulations());
  }

}
