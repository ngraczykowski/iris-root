package com.silenteight.sens.webapp.backend.configuration.solution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ConfigurationSolutionRestController {

  @NonNull
  private final SolutionsQuery solutionsQuery;

  @GetMapping("/configuration/solutions")
  public ResponseEntity<List<String>> listSolutions() {
    log.info(INTERNAL, "Listing solutions");

    List<String> solutions = solutionsQuery.list();

    log.info(INTERNAL, "Found solutions. total={}", solutions.size());

    return ok(solutions);
  }
}
