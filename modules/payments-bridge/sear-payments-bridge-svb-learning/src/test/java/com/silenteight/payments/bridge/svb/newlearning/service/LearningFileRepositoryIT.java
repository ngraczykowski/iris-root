package com.silenteight.payments.bridge.svb.newlearning.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class LearningFileRepositoryIT extends BaseDataJpaTest {

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
