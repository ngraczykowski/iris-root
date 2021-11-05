package com.silenteight.adjudication.engine.alerts.alert;

import com.silenteight.adjudication.engine.alerts.alert.AddLabelsUseCase.LabelAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AddLabelsUseCaseTest {

  private AddLabelsUseCase addLabelsUseCase;

  @BeforeEach
  void setUp() {
    var dataAccess = new InMemoryAlertLabelDataAccess();
    addLabelsUseCase = new AddLabelsUseCase(dataAccess);
  }

  @Test
  void shouldThrowCustomException() {
    var alertName = "alerts/1";
    var labels = new HashMap<String, String>();
    labels.put("name", "value");
    addLabelsUseCase.addLabels(List.of(alertName), labels);
    assertThrows(
        LabelAlreadyExistsException.class,
        () -> addLabelsUseCase.addLabels(List.of(alertName), labels));
  }
}
