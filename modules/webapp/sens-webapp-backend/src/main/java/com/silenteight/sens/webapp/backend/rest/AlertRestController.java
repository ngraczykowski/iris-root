package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.AlertCategory;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.AlertModelDto;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.AlertResponseDto;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.AlertSearchFilterDto;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.details.AlertDetailsView;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
public class AlertRestController {

  @GetMapping("/alerts")
  public ResponseEntity<AlertResponseDto> getAlerts(
      @RequestParam int offset, @RequestParam int limit, AlertSearchFilterDto searchFilter) {
    AlertResponseDto response = new AlertResponseDto(0,
        new AlertModelDto(emptyList()),
        emptyList());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/decision-tree/{decisionTreeId}/branch/{matchGroupId}/alerts")
  public ResponseEntity<AlertResponseDto> getAlerts(
      @PathVariable Long decisionTreeId,
      @PathVariable Long matchGroupId,
      @RequestParam int offset,
      @RequestParam int limit,
      @RequestParam(required = false) AlertCategory category) {

    AlertSearchFilterDto searchFilter = new AlertSearchFilterDto();
    searchFilter.setMatchGroupId(matchGroupId);
    searchFilter.setCategory(category);
    searchFilter.setDecisionTreeId(decisionTreeId);

    return getAlerts(offset, limit, searchFilter);
  }

  @GetMapping("/decision-tree/{decisionTreeId}/branch/{matchGroupId}/alert/{externalId}")
  public ResponseEntity<AlertDetailsView> getAlert(
      @PathVariable long decisionTreeId,
      @PathVariable long matchGroupId,
      @PathVariable String externalId) {

    AlertDetailsView response = null;

    return ResponseEntity.ok(response);
  }

  @GetMapping("/decision-tree/{decisionTreeId}/alert/{externalId}")
  public ResponseEntity<AlertDetailsView> getAlert(
      @PathVariable long decisionTreeId,
      @PathVariable String externalId) {

    AlertDetailsView response = null;

    return ResponseEntity.ok(response);
  }
}
