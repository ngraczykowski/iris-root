package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.Getter;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertMetaData;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class LearningAlertBatch {

  @Getter
  private final AlertMetaData alertMetaData;

  @Getter
  private final List<LearningAlert> learningAlerts = new ArrayList<>(100);
  @Getter
  private final List<ReadAlertError> errors = new ArrayList<>();

  @Getter
  private int counter = 0;

  public LearningAlertBatch(AlertMetaData alertMetaData) {
    this.alertMetaData = alertMetaData;
  }

  void addLearningAlert(LearningAlert learningAlert) {
    learningAlerts.add(learningAlert);
    counter++;
  }

  void addError(ReadAlertError error) {
    errors.add(error);
    counter++;
  }

  public List<LearningAlert> getSuccess() {
    var alertErrors = errors.stream()
        .map(ReadAlertError::getAlertId).collect(Collectors.toSet());
    return learningAlerts.stream()
        .filter(a -> !alertErrors.contains(a.getAlertId()))
        .collect(Collectors.toList());
  }

}
