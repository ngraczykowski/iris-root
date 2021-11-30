
package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.batch.exceptions.NoLearningFileFound;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningFile;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobParameters.FILE_ID_PARAMETER;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StoreCsvFileProperties.class)
@Slf4j
class StoreCsvFileStepWriterConfiguration {

  private final LearningCsvRowRepository repository;
  private final LearningDataAccess learningDataAccess;
  private final EntityManagerFactory entityManagerFactory;

  @SuppressWarnings("SpringElInspection")
  @Bean("csvFileItemReaderStepItemWriter")
  @StepScope
  LearningCsvRowWriter<LearningCsvRowEntity> jdbcLearningCsvRowWriter(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    var fileId =
        stepExecution.getJobParameters().getLong(FILE_ID_PARAMETER);
    var jobId = stepExecution.getJobExecution().getJobId();
    log.info("Executing step {} for file:{}", stepExecution.getStepName(), fileId);
    return createWriter(fileId, jobId);
  }

  private LearningFile getLearningFile(Long fileId) {
    var learningFile = learningDataAccess.findLearningFileById(fileId);
    if (learningFile.isEmpty()) {
      throw new NoLearningFileFound(String.format("Learning File not found %s", fileId));
    }
    return learningFile.get();
  }

  private LearningCsvRowWriter<LearningCsvRowEntity> createWriter(Long fileId, Long jobId) {
    var writer = new LearningCsvRowWriter<LearningCsvRowEntity>(
        repository, getLearningFile(fileId).getLearningFileId(), jobId);
    writer.setEntityManagerFactory(entityManagerFactory);
    writer.setUsePersist(true);
    return writer;
  }
}
