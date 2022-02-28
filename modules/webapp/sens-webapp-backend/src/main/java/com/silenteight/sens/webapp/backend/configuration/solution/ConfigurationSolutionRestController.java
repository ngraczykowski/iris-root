package com.silenteight.sens.webapp.backend.configuration.solution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.backend.configuration.DomainConstants.CONFIGURATION_ENDPOINT_TAG;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = CONFIGURATION_ENDPOINT_TAG)
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
