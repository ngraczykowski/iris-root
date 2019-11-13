package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.application.inbox.dto.SolveInboxMessageRequestDto;
import com.silenteight.sens.webapp.backend.presentation.dto.common.StatisticsDto;
import com.silenteight.sens.webapp.backend.presentation.dto.inbox.InboxMessageDto;
import com.silenteight.sens.webapp.backend.presentation.dto.inbox.InboxMessageSearchFilterDto;
import com.silenteight.sens.webapp.backend.presentation.dto.inbox.InboxResposneDto;
import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import javax.validation.Valid;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('INBOX_MANAGE')")
public class InboxRestController {

  @GetMapping("/inbox/stats")
  public ResponseEntity<StatisticsDto> getStats() {
    StatisticsDto response = StatisticsDto.builder()
        .total(0)
        .stats(new HashMap<>())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/inbox/messages")
  public ResponseEntity<InboxResposneDto> getMessages(
      @Valid InboxMessageSearchFilterDto searchFilter, Pageable pageable) {
    InboxResposneDto response = InboxResposneDto.builder()
        .total(0)
        .results(emptyList())
        .build();
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasAnyAuthority('INBOX_MANAGE','DECISION_TREE_LIST')")
  @GetMapping("/inbox/message")
  public ResponseEntity<InboxMessageDto> getMessage(
      @RequestParam String type, @RequestParam String referenceId) {
    InboxMessageDto response = null;
    return ResponseEntity.ok(response);
  }

  @PostMapping("/inbox/message/solve")
  public ResponseEntity<Void> solveMessage(
      @AuthenticationPrincipal SensUserDetails user,
      @Valid @RequestBody SolveInboxMessageRequestDto requestDto) {
    return ResponseEntity.noContent().build();
  }
}
