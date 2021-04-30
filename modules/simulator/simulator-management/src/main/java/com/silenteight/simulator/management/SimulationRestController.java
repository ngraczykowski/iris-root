package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.common.web.rest.RestConstants;
import com.silenteight.simulator.management.dto.CreateSimulationRequestDto;
import com.silenteight.simulator.management.dto.SimulationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
class SimulationRestController {

  static final String SIMULATIONS_URL = "/v1/simulations";

  @NonNull
  private final CreateSimulationUseCase useCase;

  @NonNull
  private final SimulationQuery simulationQuery;

  @PostMapping(SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION')")
  public ResponseEntity<Void> createDataset(
      @RequestBody @Valid CreateSimulationRequestDto createSimulationRequestDto,
      Authentication authentication) {

    CreateSimulationRequest request = CreateSimulationRequest.builder()
        .id(createSimulationRequestDto.getId())
        .simulationName(createSimulationRequestDto.getSimulationName())
        .description(createSimulationRequestDto.getDescription())
        .createdBy(authentication.getName())
        .datasets(createSimulationRequestDto.getDatasets())
        .model(createSimulationRequestDto.getModel())
        .build();
    useCase.activate(request);
    return status(CREATED).build();
  }

  @GetMapping(SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  public ResponseEntity<List<SimulationDto>> listSimulations() {
    return ok(simulationQuery.list());
  }
}
