package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateLogFileUseCaseTest {

  private GenerateLogFileUseCase generateLogFileUseCase;

  @BeforeEach
  void setUp() {
    generateLogFileUseCase = new GenerateLogFileUseCase();
  }

  @Test
  void shouldGenerateFile() {
    var error = ReadAlertError
        .builder()
        .alertId("alercik")
        .exception(new RuntimeException("couse"))
        .build();
    var file = GenerateLogFileUseCase.generateLogFile(List.of(error));
    assertTrue(file.exists());
  }
}
