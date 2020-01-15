package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class ChangelogRestController {

  @GetMapping("/changelog/{decisionTreeId}/branch/{matchGroupId}")
  public ResponseEntity<ChangelogView> getChangelog(
      @PathVariable long decisionTreeId,
      @PathVariable long matchGroupId) {

    return ResponseEntity.ok(new ChangelogView());
  }

  private static class ChangelogView {

  }
}
