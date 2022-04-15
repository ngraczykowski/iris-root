package com.silenteight.simulator.management.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.domain.SimulationState;
import com.silenteight.simulator.management.list.dto.SimulationListDto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static com.silenteight.simulator.management.domain.DomainConstants.SIMULATION_ENDPOINT_TAG;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static java.util.List.of;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = SIMULATION_ENDPOINT_TAG)
class ListSimulationRestController {

  static final String SIMULATIONS_URL = "/v1/simulations";

  @NonNull
  private final ListSimulationsQuery simulationQuery;

  @GetMapping(value = SIMULATIONS_URL, params = "state")
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  @Operation(parameters = {
      @Parameter(name = "state", in = QUERY,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = SimulationState.class)))),
      @Parameter(name = "model", in = QUERY, schema = @Schema(type = "string"))
  })
  public ResponseEntity<List<SimulationListDto>> listForStates(
      @RequestParam @Parameter(hidden = true) SimulationState... state) {
    log.info("Listing simulation in states={}", of(state));
    return ok(simulationQuery.list(of(state)));
  }

  @GetMapping(value = SIMULATIONS_URL, params = "model")
  @PreAuthorize("isAuthorized('LIST_SIMULATIONS')")
  @Hidden
  public ResponseEntity<List<SimulationListDto>> listForModel(@RequestParam String model) {
    log.info("Listing simulation with model={}", model);
    return ok(simulationQuery.findByModels(of(model)));
  }
}
