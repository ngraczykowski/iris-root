package com.silenteight.simulator.management.cancel;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.common.web.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequestMapping(RestConstants.ROOT)
@AllArgsConstructor
class CancelSimulationRestController {

  @NonNull
  private final CancelSimulationUseCase useCase;

  @PostMapping("/v1/simulations/{id}:cancel")
  @PreAuthorize("isAuthorized('CANCEL_SIMULATION')")
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
