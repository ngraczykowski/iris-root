package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto.CopyDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.copy.dto.CopyDecisionTreeResponseDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.create.dto.CreateDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.application.decisiontree.patch.dto.PatchDecisionTreeRequestDto;
import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.DecisionTreeResponseDto;
import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.DecisionTreeSearchFilterDto;
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

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
public class DecisionTreeRestController {

  private static final String CIRCUIT_BREAKER_TRIGGERED_ALERTS =
      "circuit_breaker_triggered_alerts.csv";
  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @GetMapping("/decision-trees")
  @PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
  public ResponseEntity<DecisionTreeResponseDto> getAll(
      DecisionTreeSearchFilterDto searchFilter) {
    DecisionTreeResponseDto response = new DecisionTreeResponseDto(0, emptyList());
    return ResponseEntity.ok(response);
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

  @GetMapping("/decision-tree/{id}")
  public ResponseEntity<DecisionTreeDto> getDecisionTreeDetails(@PathVariable long id) {
    DecisionTreeDto response = null;
    return ResponseEntity.ok(response);
  }

  @GetMapping("/alert/{externalId}/decision-trees")
  public ResponseEntity<DecisionTreeResponseDto> getAlertDecisionTrees(
      @PathVariable String externalId) {
    DecisionTreeResponseDto response = new DecisionTreeResponseDto(0, emptyList());
    return ResponseEntity.ok(response);
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
