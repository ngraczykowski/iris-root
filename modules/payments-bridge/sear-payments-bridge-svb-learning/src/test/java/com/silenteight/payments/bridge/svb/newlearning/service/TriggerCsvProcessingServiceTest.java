package com.silenteight.payments.bridge.svb.newlearning.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.svb.newlearning.port.FileListPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriggerCsvProcessingServiceTest {

  @Mock
  private FileListPort fileListPort;
  private LearningFileRepository learningFileRepository = new InMemoryLearningFileRepository();
  private TriggerCsvProcessingService triggerCsvProcessingService;

  @BeforeEach
  void setUp() {
    triggerCsvProcessingService =
        new TriggerCsvProcessingService(fileListPort, learningFileRepository);

    var object = ObjectPath.builder().name("analystdecison-2-hits.csv").bucket("bucket").build();
    var object2 = ObjectPath.builder().name("analystdecison-2-hits2.csv").bucket("bucket").build();

    when(fileListPort.getFilesList()).thenReturn(List.of(object, object2));
  }

  @Test
  void shouldInsertTwoFiles() {
    triggerCsvProcessingService.process();
    triggerCsvProcessingService.process();
    triggerCsvProcessingService.process();

    assertThat(learningFileRepository.findAll().size()).isEqualTo(2);
  }
}
