package com.silenteight.sens.webapp.backend.decisiontree.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeFacade;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@SuppressWarnings("squid:S1200")
class DecisionTreeRestController {

  @NonNull
  private final DecisionTreeFacade decisionTreeFacade;

  private static final String CIRCUIT_BREAKER_TRIGGERED_ALERTS =
      "circuit_breaker_triggered_alerts.csv";
  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @GetMapping("/decision-trees")
  @PreAuthorize("hasAuthority('DECISION_TREE_LIST') || hasRole('DECISION_TREE_VIEWER')")
  public ResponseEntity<DecisionTreesDto> list() {
    log.debug("Requesting Decision Trees");
    return ok(decisionTreeFacade.list());
  }

  @GetMapping("/decision-trees/{id}")
  public ResponseEntity<DecisionTreeDetailsDto> details(@PathVariable long id) {
    log.debug("Requesting Decision Tree details. decisionTreeId={}", id);
    return ok(decisionTreeFacade.details(id));
  }
}
