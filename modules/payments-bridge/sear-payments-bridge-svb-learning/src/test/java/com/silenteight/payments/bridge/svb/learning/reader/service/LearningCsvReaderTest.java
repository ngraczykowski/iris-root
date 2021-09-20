package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningCsvReaderTest {

  private LearningCsvReader learningCsvReader;
  @Mock
  private ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;
  @Mock
  private CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @BeforeEach
  void setUp() {
    var createMatch =
        new CreateMatchUseCase(extractAlertedPartyDataUseCase, createAlertedPartyEntitiesUseCase);
    learningCsvReader =
        new LearningCsvReader(new CsvFileProviderTestImpl(), new CreateAlertUseCase(createMatch));
  }

  @Test
  void shouldReadAlerts() {
    when(extractAlertedPartyDataUseCase.extractAlertedPartyData(any())).thenReturn(
        AlertedPartyData.builder().build());

    List<LearningAlert> learningAlerts = new ArrayList<>();
    learningCsvReader.read(learningAlerts::add);

    assertThat(learningAlerts.size()).isEqualTo(141);
  }
}
