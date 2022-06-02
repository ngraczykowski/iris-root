package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableConfigurationProperties(ProcessConfigurationProperties.class)
class ProcessConfiguration {

  @Bean
  SolvingAlertReceivedProcess alertAgentDispatcherProcess(
      AgentExchangeAlertSolvingMapper agentExchnageRequestMapper,
      AgentsMatchPublisher agentsMatchPublisher,
      MatchFeaturesFacade matchFeaturesFacade,
      AlertSolvingRepository alertSolvingRepository,
      ReadyMatchFeatureVectorPublisher governanceProvider,
      CommentInputResolveProcess commentInputResolveProcess,
      CategoryResolveProcess categoryResolveProcess) {

    return new SolvingAlertReceivedProcess(
        agentExchnageRequestMapper,
        agentsMatchPublisher,
        matchFeaturesFacade,
        alertSolvingRepository,
        governanceProvider,
        commentInputResolveProcess,
        categoryResolveProcess);
  }

  @Bean
  public CommentInputResolveProcess commentInputResolveProcess(
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClient commentInputClient,
      final CommentInputClientRepository commentInputClientRepository,
      final HazelcastInstance hazelcastInstance,
      final ProcessConfigurationProperties processConfigurationProperties) {

    final IQueue<String> alertCommentsInputQueue =
        hazelcastInstance.getQueue("alert.comments.inputs");

    final ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(
            processConfigurationProperties.getCommentInputProcess().getPoolSize());

    return new CommentInputResolveProcess(
        commentInputClient,
        converter,
        commentInputClientRepository,
        scheduledExecutorService,
        alertCommentsInputQueue);
  }

  @Bean
  public CategoryResolveProcess categoryResolveProcess(
      CategoryValuesClient categoryValueClient,
      AlertSolvingRepository alertSolvingRepository,
      final HazelcastInstance hazelcastInstance,
      ReadyMatchFeatureVectorPublisher readyMatchFeatureVectorPublisher,
      final ProcessConfigurationProperties processConfigurationProperties) {

    final IQueue<Long> alertCommentsInputQueue = hazelcastInstance.getQueue("alert.category.value");

    final ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(
            processConfigurationProperties.getCategoryProcess().getPoolSize());

    return new CategoryResolveProcess(
        categoryValueClient,
        scheduledExecutorService,
        alertCommentsInputQueue,
        alertSolvingRepository,
        readyMatchFeatureVectorPublisher);
  }

  @Bean
  SolvedAlertProcess resolvedAlertProcess(
      AlertSolvingRepository alertSolvingRepository,
      RecommendationPublisher recommendationPublisher,
      CommentFacade commentFacade,
      AlertSolvingAlertContextMapper alertSolvingAlertContextMapper,
      RecommendationFacade recommendationFacade,
      ProtoMessageToObjectNodeConverter converter,
      CommentInputClientRepository commentInputClientRepository,
      final ProcessConfigurationProperties processConfigurationProperties) {
    return new SolvedAlertProcess(
        recommendationPublisher,
        alertSolvingRepository,
        alertSolvingAlertContextMapper,
        commentFacade,
        recommendationFacade,
        converter,
        commentInputClientRepository,
        processConfigurationProperties.getSolvedAlertProcess());
  }

  @Bean
  AgentResponseProcess agentResponseProcess(
      final ReadyMatchFeatureVectorPublisher governanceProvider,
      AlertSolvingRepository alertSolvingRepository,
      ProtoMessageToObjectNodeConverter converter) {
    return new AgentResponseProcess(governanceProvider, alertSolvingRepository, converter);
  }

  @Bean
  GovernanceMatchResponseProcess governanceMatchResponseProcess(
      final AlertSolvingRepository alertSolvingRepository,
      final HazelcastInstance hazelcastInstance,
      final GovernanceFacade governanceFacade,
      final SolvedAlertProcess solvedAlertProcess,
      final ProcessConfigurationProperties processConfigurationProperties) {
    final ExecutorService scheduledExecutorService =
        Executors.newFixedThreadPool(
            processConfigurationProperties.getGovernanceProcess().getPoolSize());
    final GovernanceAlertPublisher governanceAlertPublisher =
        new GovernanceAlertPublisher(
            governanceFacade, hazelcastInstance, scheduledExecutorService, solvedAlertProcess);
    return new GovernanceMatchResponseProcess(governanceAlertPublisher, alertSolvingRepository);
  }
}
