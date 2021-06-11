package com.silenteight.serp.governance.qa.validation.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

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
class ListAlertValidationRestController {

  private static final String ALERTS_LIST_URL = "/v1/qa/1/alerts";
  private static final String HEADER_TOTAL_ITEMS = "X-Total-Items";
  private static final String HEADER_NEXT_ITEM = "Next-Page-Token";
  private static final String MIN_DATE = "1970-01-01T00:00:00+00";
  @NonNull
  private final ListAlertValidationQuery listAlertValidationQuery;

  @GetMapping(value = ALERTS_LIST_URL, params = {"state", "pageSize"})
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION')")
  public ResponseEntity<List<AlertValidationDto>> list(
      @RequestParam List<DecisionState> state,
      @RequestParam int pageSize,
      @RequestParam(required = false, defaultValue = MIN_DATE) String pageToken) {

    List<AlertValidationDto> list = listAlertValidationQuery.list(state, parse(pageToken),
        pageSize);
    return ok()
        .header(HEADER_TOTAL_ITEMS, valueOf(listAlertValidationQuery.count(state)))
        .header(HEADER_NEXT_ITEM, valueOf(getHeaderNextItem(list)))
        .body(list);
  }

  private static Instant getHeaderNextItem(List<AlertValidationDto> list) {
    return !list.isEmpty() ? list.get(list.size() - 1).getAddedAt() : null;
  }
}

