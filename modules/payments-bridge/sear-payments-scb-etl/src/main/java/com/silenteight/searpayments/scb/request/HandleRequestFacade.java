package com.silenteight.searpayments.scb.request;

import com.silenteight.searpayments.scb.mapper.CreateAlertsFromRequestFactory;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.Alert.AlertStatus;
import com.silenteight.searpayments.scb.domain.Alert.DamageReason;
import com.silenteight.searpayments.scb.domain.AlertService;
import com.silenteight.searpayments.bridge.dto.input.RequestDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
//@Slf4j
//public class HandleRequestFacade {
//
//
//  @NonNull private final CreateAlertsFromRequestFactory createAlertsFromRequestFactory;
//  @NonNull private final ProcessAlerts processAlerts;
//  @NonNull private final PublishAlerts publishAlerts;
//  @NonNull private final AlertService alertService;
//
//  @Secured("ROLE_CMAPI")
//  @NotNull
//  public List<Long> invoke(@NonNull RequestDto requestDto) {
//    log.debug("invoked");
//
//    List<Alert> alerts = createAlertsFromRequestFactory.create(requestDto).create();
//    processAlerts.invoke(alerts);
//    var responseDto = publishAlerts.invoke(new PublishAlerts.RequestDto(alerts));
//
//    var failedAlerts = responseDto.getFailedAlerts();
//    if (!failedAlerts.isEmpty()) {
//      markAlertsAsFailed(failedAlerts);
//    }
//
//    var completedAlertIds = responseDto
//        .getCompletedAlerts()
//        .stream()
//        .map(Alert::getId)
//        .collect(Collectors.toList());
//
//    log.debug("Alerts published successfully: {}", completedAlertIds);
//    return completedAlertIds;
//  }
//
//  private void markAlertsAsFailed(List<Alert> failedAlerts) {
//    var alertIds = failedAlerts
//        .stream()
//        .map(Alert::getId)
//        .collect(Collectors.toList());
//
//    log.debug("batch update not published alerts as damaged: {}", alertIds);
//
//    alertService.batchUpdateAlertStatusAndDamageReason(alertIds,
//        AlertStatus.STATE_DAMAGED, new DamageReason(
//            "INITIAL_PUBLISH_FAILED",
//            "Failed to publish the alert at initial stage"));
//  }
//}
