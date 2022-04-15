package com.silenteight.serp.governance.qa.manage.validation.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.DecisionState;
import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.qa.manage.domain.DomainConstants.QA_ENDPOINT_TAG;
import static java.lang.String.valueOf;
import static java.time.OffsetDateTime.parse;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = QA_ENDPOINT_TAG)
class ListAlertValidationRestController {

  private static final String ALERTS_LIST_URL = "/v1/qa/1/alerts";
  private static final String HEADER_TOTAL_ITEMS = "X-Total-Items";
  private static final String HEADER_NEXT_ITEM = "Next-Page-Token";
  private static final String MIN_DATE = "1970-01-01T00:00:00+00";
  @NonNull
  private final ListAlertValidationQuery listAlertValidationQuery;

  @GetMapping(value = ALERTS_LIST_URL, params = {"state", "pageSize"})
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION,
          content = @Content)
  })
  public ResponseEntity<List<AlertValidationDto>> list(
      @RequestParam List<DecisionState> state,
      @RequestParam int pageSize,
      @RequestParam(required = false, defaultValue = MIN_DATE) String pageToken) {

    List<AlertValidationDto> list = listAlertValidationQuery
        .list(state, parse(pageToken), pageSize);
    int count = listAlertValidationQuery.count(state);
    log.debug("Returning a list of QA Alerts (listSize={}, all={}", list.size(), count);
    return ok()
        .header(HEADER_TOTAL_ITEMS, valueOf(count))
        .header(HEADER_NEXT_ITEM, valueOf(getHeaderNextItem(list, pageSize)))
        .body(list);
  }

  private static Instant getHeaderNextItem(List<AlertValidationDto> list, Integer pageSize) {
    if (list.size() < pageSize)
      return null;

    return list.get(list.size() - 1).getAddedAt();
  }
}

