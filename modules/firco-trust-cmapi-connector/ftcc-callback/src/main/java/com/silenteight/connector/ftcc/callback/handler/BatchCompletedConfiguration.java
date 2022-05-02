package com.silenteight.connector.ftcc.callback.handler;

import lombok.NonNull;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@Configuration
@EnableJpaRepositories
@EntityScan
class BatchCompletedConfiguration {

  @Bean
  BatchCompletedService batchCompletedService(
      @NonNull BatchCompletedRepository batchCompletedRepository,
      @NonNull PartitionCreator partitionCreator,
      @NonNull Clock clock) {

    return new BatchCompletedService(batchCompletedRepository, partitionCreator, clock);
  }
}
