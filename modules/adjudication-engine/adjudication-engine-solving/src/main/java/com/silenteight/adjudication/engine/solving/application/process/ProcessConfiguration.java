package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.publisher.AgentsMatchPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.GovernanceAlertPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.GovernanceMatchPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class ProcessConfiguration {

  @Bean
  AlertAgentDispatchProcess alertAgentDispatcherProcess(
      AgentExchangeAlertSolvingMapper agentExchnageRequestMapper,
      AgentsMatchPublisher agentsMatchPublisher,
      MatchFeaturesFacade matchFeaturesFacade,
      AlertSolvingRepository alertSolvingRepository
  ) {
    return new AlertAgentDispatchProcess(agentExchnageRequestMapper,
        agentsMatchPublisher, matchFeaturesFacade, alertSolvingRepository);
  }


  @Bean
  ResolvedAlertProcess resolvedAlertProcess(
      AlertSolvingRepository alertSolvingRepository,
      RecommendationPublisher recommendationPublisher,
      CommentFacade commentFacade,
      AlertSolvingAlertContextMapper alertSolvingAlertContextMapper,
      RecommendationFacade recommendationFacade,
      ProtoMessageToObjectNodeConverter converter) {
    return new ResolvedAlertProcess(
        recommendationPublisher, alertSolvingRepository, alertSolvingAlertContextMapper,
        commentFacade, recommendationFacade, converter);
  }

  @Bean
  AgentResponseProcess gamma(
      final GovernanceMatchPublisher governanceProvider,
      AlertSolvingRepository alertSolvingRepository,
      ProtoMessageToObjectNodeConverter converter) {
    return new AgentResponseProcess(governanceProvider, alertSolvingRepository, converter);
  }

  @Bean
  SomethingSolution somethingSolution(
      final GovernanceMatchPublisher governanceProvider,
      final AlertSolvingRepository alertSolvingRepository
  ) {
    return new SomethingSolution(governanceProvider, alertSolvingRepository);
  }

  @Bean
  GovernanceMatchResponseProcess governanceMatchResponseProcess(
      final AlertSolvingRepository alertSolvingRepository,
      final HazelcastInstance hazelcastInstance,
      final GovernanceFacade governanceFacade,
      final ResolvedAlertProcess resolvedAlertProcess
  ) {
    final ExecutorService scheduledExecutorService = Executors.newFixedThreadPool(15);
    final GovernanceAlertPublisher governanceAlertPublisher =
        new GovernanceAlertPublisher(
            governanceFacade, hazelcastInstance, scheduledExecutorService, resolvedAlertProcess);
    return new GovernanceMatchResponseProcess(
        governanceAlertPublisher,
        alertSolvingRepository);
  }
}
