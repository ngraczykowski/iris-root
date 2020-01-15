package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.match.MatchResponseDto;
import com.silenteight.sens.webapp.backend.presentation.dto.match.MatchSearchFilterDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
public class MatchRestController {

  @GetMapping("/alert/{alertId}/matches")
  public ResponseEntity<MatchResponseDto> getMatchesForAlertInMatchGroup(
      @PathVariable Long alertId, MatchSearchFilterDto filter, Pageable pageable) {
    MatchResponseDto response = new MatchResponseDto(new PageImpl<>(emptyList(), pageable, 0));

    return ResponseEntity.ok(response);
  }
}
