package com.silenteight.payments.bridge.app.learning;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.PaymentsBridgeApplication;
import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = { PaymentsBridgeApplication.class })
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@Slf4j
@ActiveProfiles({ "mockae", "mockdatasource", "mockgovernance", "mockagents", "mockaws", "test" })
class LearningFileRepositoryIT {

  @Autowired
  private LearningFileRepository learningFileRepository;

  private final String fileName = UUID.randomUUID().toString();

  @Test
  @Disabled
  void shouldSelectExistingFile() {
    learningFileRepository.save(LearningFileEntity
        .builder()
        .id(10L)
        .bucketName("bucket")
        .fileName(fileName)
        .status(CsvProcessingFileStatus.NEW.toString())
        .build());
    assertThat(
        learningFileRepository
            .findAllByFileNameAndBucketName(fileName, "bucket")
            .size()).isEqualTo(
        1);
  }
}
