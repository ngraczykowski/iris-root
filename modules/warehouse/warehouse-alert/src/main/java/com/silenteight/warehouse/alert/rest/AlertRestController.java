package com.silenteight.warehouse.alert.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.alert.rest.service.AlertNotFoundException;
import com.silenteight.warehouse.alert.rest.service.AlertProvider;
import com.silenteight.warehouse.common.web.request.AlertResource;
import com.silenteight.warehouse.indexer.query.dto.AlertDetailsRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class AlertRestController {

  private static final String QA_ALERT_LIST_URL = "/v1/analysis/production/query";
  private static final String QA_ALERT_DETAIL_URL = "/v1/analysis/production/alerts/{id}";

  @NonNull
  private final AlertProvider alertProvider;

  @GetMapping(QA_ALERT_LIST_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Collection<Map<String, String>>> getAlertsList(
      @RequestParam(name = "fields") List<String> fields,
      @RequestParam(name = "name") List<String> names) {

    log.debug("Getting alerts list, fields={}, names={}",fields,names);
    return ok().body(alertProvider.getMultipleAlertsAttributes(fields, names));
  }

  @GetMapping(QA_ALERT_DETAIL_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Map<String, String>> getSingleAlert(
      @PathVariable("id") String id, @RequestParam(name = "fields") List<String> fields)
      throws AlertNotFoundException {

    log.debug("Getting single alert, alertId={}, fields={}", id,fields);
    return ok().body(alertProvider.getSingleAlertAttributes(fields, id));
  }

  @PostMapping(QA_ALERT_DETAIL_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Map<String, String>> getSingleAlert(
      @PathVariable("id") String id, @RequestBody @Valid AlertDetailsRequest alertDetailsRequest)
      throws AlertNotFoundException {

    log.debug("Getting single alert, alertId={}, fields={}", id, alertDetailsRequest.getFields());
    return ok().body(alertProvider.getSingleAlertAttributes(alertDetailsRequest.getFields(),
        toResourceName(id)));
  }

  private static String toResourceName(String value) {
    return AlertResource.toResourceName(fromString(value));
  }
}
