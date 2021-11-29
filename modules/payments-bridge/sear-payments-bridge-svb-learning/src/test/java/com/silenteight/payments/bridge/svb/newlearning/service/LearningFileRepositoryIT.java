package com.silenteight.payments.bridge.svb.newlearning.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.testing.PbBaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LearningFileRepositoryIT extends PbBaseDataJpaTest {

  @Autowired
  private LearningFileRepository learningFileRepository;

  @Test
  void shouldSaveFileName() {
    learningFileRepository.save(LearningFileEntity
        .builder()
        .id(0L)
        .bucketName("bucket")
        .fileName("fileNAme")
        .status(CsvProcessingFileStatus.NEW.toString())
        .build());
    assertThat(learningFileRepository.findAll().size()).isEqualTo(1);
  }

  @Test
  void shouldSelectExistingFile() {
    learningFileRepository.save(LearningFileEntity
        .builder()
        .id(0L)
        .bucketName("bucket")
        .fileName("fileNAme")
        .status(CsvProcessingFileStatus.NEW.toString())
        .build());
    assertThat(
        learningFileRepository
            .findAllByFileNameAndBucketName("fileNAme", "bucket")
            .size()).isEqualTo(
        1);
  }
}
