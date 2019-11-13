package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.SolutionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.http.HttpStatus.SEE_OTHER;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('SOLUTION_VIEW')")
@Slf4j
public class SolutionRestController {

  @GetMapping("/solution")
  public ResponseEntity<SolutionDto> getSolution(@RequestParam String externalId) {
    SolutionDto response = null;
    return ResponseEntity.ok(response);
  }

  @PostMapping("/solution")
  public RedirectView postSolution(@RequestBody AlertGuideDto alertGuidance) {
    String externalId = "externalId";

    RedirectView redirectView = new RedirectView(RestConstants.ROOT + "/solution", true);
    redirectView.addStaticAttribute("externalId", externalId);
    redirectView.setStatusCode(SEE_OTHER);
    return redirectView;
  }
}
