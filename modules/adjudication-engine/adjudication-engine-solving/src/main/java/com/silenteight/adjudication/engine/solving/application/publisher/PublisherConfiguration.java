package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.GovernanceMatchResponseProcess;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class PublisherConfiguration {

  @Bean
  GovernanceMatchPublisher governancePublisher(
      GovernanceFacade governanceFacade,
      HazelcastInstance hazelcastInstance,
      GovernanceMatchResponseProcess governanceMatchResponseProcess,
      ProtoMessageToObjectNodeConverter converter) {
    final ExecutorService scheduledExecutorService = Executors.newFixedThreadPool(15);
    return new GovernanceMatchPublisher(
        governanceFacade, hazelcastInstance, scheduledExecutorService,
        governanceMatchResponseProcess, converter);
  }
}
