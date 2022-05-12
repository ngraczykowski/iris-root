package com.silenteight.warehouse.alert.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.alert.rest.service.AlertProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @NonNull
  private final AlertProvider alertProvider;

  @GetMapping(QA_ALERT_LIST_URL)
  @PreAuthorize("isAuthorized('VIEW_ALERTS_DATA')")
  public ResponseEntity<Collection<Map<String, String>>> getAlertDetails(
      @RequestParam(name = "fields") List<String> fields,
      @RequestParam(name = "name") List<String> names) {

    log.debug("Getting alerts list, fields={}, names={}", fields, names);
    return ok().body(alertProvider.getMultipleAlertsAttributes(fields, names));
  }
}
