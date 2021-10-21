package com.silenteight.payments.bridge.svb.learning.reader.service;

import com.silenteight.payments.bridge.etl.firco.parser.MessageParserFacade;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractFieldValueUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class LearningCsvReaderTest {

  private ProcessAlertService learningCsvReader;
  @Mock
  private ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;
  @Mock
  private CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;
  @Mock
  private ExtractMessageStructureUseCase extractMessageStructureUseCase;
  @Mock
  private ExtractFieldValueUseCase extractFieldValueUseCase;
  @Mock
  private MessageParserFacade messageParserFacade;

  @BeforeEach
  void setUp() {
    var etlMatchService = new EtlMatchService(
        extractAlertedPartyDataUseCase, createAlertedPartyEntitiesUseCase,
        extractMessageStructureUseCase, extractFieldValueUseCase, messageParserFacade);

    learningCsvReader = new ProcessAlertService(
        new CsvFileProviderTestImpl(),
        new EtlAlertService(etlMatchService, new EtlAlertServiceProperties()));
  }

  @Test
  void shouldReadAlerts() {
    when(extractAlertedPartyDataUseCase.extractAlertedPartyData(any(), any(), any(),
        any())).thenReturn(
        AlertedPartyData.builder().build());

    List<LearningAlert> learningAlerts = new ArrayList<>();
    learningCsvReader.read(LearningRequest.builder().build(), learningAlerts::add);

    assertThat(learningAlerts.size()).isEqualTo(163);
  }
}
