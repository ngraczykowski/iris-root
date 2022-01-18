package com.silenteight.payments.bridge.app.learning;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriggerCsvProcessingServiceTest {

  @Mock
  private CsvFileResourceProvider csvFileResourceProvider;
  @Mock
  private LearningRunnerService learningRunnerService;
  private final LearningFileRepository learningFileRepository =
      new InMemoryLearningFileRepository();
  private LearningCsvFileTrigger triggerCsvLearning;


  @BeforeEach
  void setUp() {
    triggerCsvLearning =
        new LearningCsvFileTrigger(
            learningRunnerService, csvFileResourceProvider, learningFileRepository);
    var object = ObjectPath.builder().name("analystdecison-2-hits.csv").bucket("bucket").build();

    when(csvFileResourceProvider.getFilesList()).thenReturn(List.of(object));
  }

  @Test
  void shouldInsertTwoFiles() {
    triggerCsvLearning.process();
    triggerCsvLearning.process();
    triggerCsvLearning.process();

    assertThat(learningFileRepository
        .findAllByFileNameAndBucketName("analystdecison-2-hits.csv", "bucket")
        .size()).isEqualTo(2);
  }
}
