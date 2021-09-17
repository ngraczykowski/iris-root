package com.silenteight.warehouse.indexer.query.single;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class AlertRestController {

  private static final String QA_ALERT_LIST_URL = "/v1/analysis/production/query";
  private static final String QA_ALERT_DETAIL_URL = "/v1/analysis/production/alerts/{id}";

  @NonNull
  private final AlertQueryService alertQueryService;

  @GetMapping(QA_ALERT_LIST_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Collection<Map<String, String>>> getAlertsList(
      @RequestParam(name = "fields") List<String> fields,
      @RequestParam(name = "name") List<String> names) {

    log.debug("Getting alerts list, fields={}, names={}",fields,names);
    return ok().body(alertQueryService.getMultipleAlertsAttributes(fields, names));
  }

  @GetMapping(QA_ALERT_DETAIL_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Map<String, String>> getSingleAlert(
      @PathVariable("id") String id, @RequestParam(name = "fields") List<String> fields) {

    log.debug("Getting single alert, alertId={}, fields={}", id,fields);
    return ok().body(alertQueryService.getSingleAlertAttributes(fields, id));
  }
}
