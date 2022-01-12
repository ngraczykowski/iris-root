package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.BatchAddLabelsRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertLabelUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class AddAlertLabelService implements AddAlertLabelUseCase {

  private final AlertClientPort alertClient;

  @Override
  public void invoke(List<String> alertNames, List<Label> labels) {

    var request = createRequest(alertNames, labels);

    log.info(
        "Adding new labels: labelsCount={}, labels={}", alertNames.size(),
        request.getLabelsMap());

    var response = alertClient.batchAddLabels(request);

    log.info(
        "New labels added, count={}, labels={}", response.getLabelsCount(), request.getLabelsMap());
  }

  public static BatchAddLabelsRequest createRequest(List<String> alertNames, List<Label> labels) {

    var labelMap = labels.stream()
        .collect(Collectors.toMap(Label::getName, Label::getValue));

    return BatchAddLabelsRequest.newBuilder()
        .addAllAlerts(alertNames)
        .putAllLabels(labelMap)
        .build();
  }
}
