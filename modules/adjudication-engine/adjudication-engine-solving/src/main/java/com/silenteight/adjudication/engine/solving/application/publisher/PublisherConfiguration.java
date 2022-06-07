package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.port.GovernanceMatchResponsePort;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvedAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class PublisherConfiguration {

  @Bean
  ReadyMatchFeatureVectorPort readyMatchFeatureVectorPort(
      GovernanceFacade governanceFacade,
      Queue governanceMatchToSendQueue,
      GovernanceMatchResponsePort governanceMatchResponseProcess,
      ProtoMessageToObjectNodeConverter converter) {
    final ExecutorService scheduledExecutorService = Executors.newFixedThreadPool(15);
    return new ReadyMatchFeatureVectorPublisher(
        governanceFacade,
        governanceMatchToSendQueue,
        scheduledExecutorService,
        governanceMatchResponseProcess,
        converter);
  }

  @Bean
  GovernanceAlertPublisher governanceAlertPublisher(
      GovernanceFacade governanceFacade,
      Queue governanceAlertsToSendQueue,
      SolvedAlertPort solvedAlertProcess) {
    final ExecutorService scheduledExecutorService = Executors.newFixedThreadPool(15);
    return new GovernanceAlertPublisher(
        governanceFacade, governanceAlertsToSendQueue, scheduledExecutorService, solvedAlertProcess);
  }
}
