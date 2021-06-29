package com.silenteight.serp.governance.qa.manage.analysis.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.lang.String.valueOf;
import static java.time.OffsetDateTime.parse;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ListAlertAnalysisRestController {

  private static final String ALERTS_LIST_URL = "/v1/qa/0/alerts";
  private static final String HEADER_TOTAL_ITEMS = "X-Total-Items";
  private static final String HEADER_NEXT_ITEM = "Next-Page-Token";
  private static final String MIN_DATE = "1970-01-01T00:00:00+00";
  @NonNull
  private final ListAlertQuery listAlertQuery;

  @GetMapping(value = ALERTS_LIST_URL, params = {"state", "pageSize"})
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  public ResponseEntity<List<AlertAnalysisDto>> list(
      @RequestParam List<DecisionState> state,
      @RequestParam Integer pageSize,
      @RequestParam(required = false, defaultValue = MIN_DATE) String pageToken) {

    List<AlertAnalysisDto> list = listAlertQuery.list(state, parse(pageToken), pageSize);
    int count = listAlertQuery.count(state);
    return ok()
        .header(HEADER_TOTAL_ITEMS, valueOf(count))
        .header(HEADER_NEXT_ITEM, valueOf(getHeaderNextItem(list)))
        .body(list);
  }

  private static Instant getHeaderNextItem(List<AlertAnalysisDto> list) {
    return !list.isEmpty() ? list.get(list.size() - 1).getAddedAt() : null;
  }
}

