package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.publisher.AgentsMatchPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.GovernanceAlertPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.ReadyMatchFeatureVectorPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
class ProcessConfiguration {

  @Bean
  AlertAgentDispatchProcess alertAgentDispatcherProcess(
      AgentExchangeAlertSolvingMapper agentExchnageRequestMapper,
      AgentsMatchPublisher agentsMatchPublisher,
      MatchFeaturesFacade matchFeaturesFacade,
      AlertSolvingRepository alertSolvingRepository,
      ReadyMatchFeatureVectorPublisher governanceProvider,
      CommentInputResolveProcess commentInputResolveProcess

  ) {
    return new AlertAgentDispatchProcess(agentExchnageRequestMapper,
        agentsMatchPublisher, matchFeaturesFacade, alertSolvingRepository, governanceProvider,
        commentInputResolveProcess
    );
  }

  @Bean
  public CommentInputResolveProcess commentInputResolveProcess(
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClient commentInputClient,
      final CommentInputClientRepository commentInputClientRepository,
      final HazelcastInstance hazelcastInstance
  ) {

    final IQueue<String> alertCommentsInputQueue =
        hazelcastInstance.getQueue("alert.comments.inputs");

    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    return new CommentInputResolveProcess(commentInputClient, converter,
        commentInputClientRepository,
        scheduledExecutorService,
        alertCommentsInputQueue
    );
  }


  @Bean
  ResolvedAlertProcess resolvedAlertProcess(
      AlertSolvingRepository alertSolvingRepository,
      RecommendationPublisher recommendationPublisher,
      CommentFacade commentFacade,
      AlertSolvingAlertContextMapper alertSolvingAlertContextMapper,
      RecommendationFacade recommendationFacade,
      ProtoMessageToObjectNodeConverter converter,
      CommentInputClientRepository commentInputClientRepository
  ) {
    return new ResolvedAlertProcess(
        recommendationPublisher, alertSolvingRepository, alertSolvingAlertContextMapper,
        commentFacade, recommendationFacade, converter, commentInputClientRepository);
  }

  @Bean
  AgentResponseProcess gamma(
      final ReadyMatchFeatureVectorPublisher governanceProvider,
      AlertSolvingRepository alertSolvingRepository,
      ProtoMessageToObjectNodeConverter converter) {
    return new AgentResponseProcess(governanceProvider, alertSolvingRepository, converter);
  }

  @Bean
  SomethingSolution somethingSolution(
      final ReadyMatchFeatureVectorPublisher governanceProvider,
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
