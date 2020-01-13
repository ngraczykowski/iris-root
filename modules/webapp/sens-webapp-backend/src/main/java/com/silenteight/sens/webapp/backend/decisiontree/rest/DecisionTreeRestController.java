package com.silenteight.sens.webapp.backend.decisiontree.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto.CopyDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto.CopyDecisionTreeResponseDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.create.dto.CreateDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.patch.dto.PatchDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeFacade;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static java.util.Collections.emptyList;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasRole('view-decision-trees')")
class DecisionTreeRestController {

  @NonNull
  private final DecisionTreeFacade decisionTreeFacade;

  private static final String CIRCUIT_BREAKER_TRIGGERED_ALERTS =
      "circuit_breaker_triggered_alerts.csv";
  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @GetMapping("/decision-trees")
  public ResponseEntity<DecisionTreesDto> list() {
    log.debug("Requesting Decision Trees");

    return ok(decisionTreeFacade.list());
  }

  @DeleteMapping("/decision-tree/{id}")
  public ResponseEntity<Void> remove(@PathVariable long id) {
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/decision-tree/copy")
  public ResponseEntity<CopyDecisionTreeResponseDto> copy(
      @Valid @RequestBody CopyDecisionTreeRequestDto requestDto) {
    CopyDecisionTreeResponseDto response = new CopyDecisionTreeResponseDto();
    return ResponseEntity.accepted().body(response);
  }

  @PatchMapping("/decision-tree/{id}")
  public ResponseEntity<Void> patchDecisionTree(
      @PathVariable Long id, @Valid @RequestBody PatchDecisionTreeRequestDto requestDto) {
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/decision-tree")
  public ResponseEntity<Void> createDecisionTree(
      @Valid @RequestBody CreateDecisionTreeRequestDto requestDto) {

    long decisionTreeId = 0L;

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(decisionTreeId)
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping("/decision-trees/{id}/branches")
  public ResponseEntity<DecisionTreeDto> getDecisionTreeDetails(@PathVariable long id) {
    DecisionTreeDto response = null;
    return ok(response);
  }

  @GetMapping("/alert/{externalId}/decision-trees")
  public ResponseEntity<DecisionTreesDto> getAlertDecisionTrees(
      @PathVariable String externalId) {
    DecisionTreesDto response = new DecisionTreesDto(emptyList());
    return ok(response);
  }

  @GetMapping("/decision-tree/{id}/circuit-breaker-triggered-alerts")
  public void getWronglySolvedAlertsForDecisionTree(HttpServletResponse response,
                                                    @PathVariable long id) throws IOException {
    CsvBuilder<AlertsFromWronglySolvedBranchesAuditDto> csvBuilder =
        new CsvBuilder<>(new ArrayList<AlertsFromWronglySolvedBranchesAuditDto>().stream());
    csvResponseWriter.write(response, CIRCUIT_BREAKER_TRIGGERED_ALERTS, csvBuilder);
  }

  private static class AlertsFromWronglySolvedBranchesAuditDto {

  }
}
