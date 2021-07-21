package com.silenteight.simulator.management.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.common.web.rest.RestConstants;
import com.silenteight.simulator.management.create.dto.CreateSimulationRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
class CreateSimulationRestController {

  static final String SIMULATIONS_URL = "/v1/simulations";

  @NonNull
  private final CreateSimulationUseCase useCase;

  @PostMapping(SIMULATIONS_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION')")
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateSimulationRequestDto createSimulationRequestDto,
      Authentication authentication) {

    log.info("Creating simulation. CreateSimulationRequestDto={}", createSimulationRequestDto);
    CreateSimulationRequest request = CreateSimulationRequest.builder()
        .id(createSimulationRequestDto.getId())
        .simulationName(createSimulationRequestDto.getSimulationName())
        .description(createSimulationRequestDto.getDescription())
        .createdBy(authentication.getName())
        .datasets(createSimulationRequestDto.getDatasets())
        .model(createSimulationRequestDto.getModel())
        .build();
    useCase.activate(request);

    log.debug("Create simulation request processed.");
    return status(CREATED).build();
  }
}
