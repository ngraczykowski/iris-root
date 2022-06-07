package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.port.AgentsMatchPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.GovernanceAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableConfigurationProperties(ProcessConfigurationProperties.class)
class ProcessConfiguration {

  @Bean
  SolvingAlertReceivedProcess alertAgentDispatcherProcess(
      AgentExchangeAlertSolvingMapper agentExchnageRequestMapper,
      AgentsMatchPort agentsMatchPublisher,
      MatchFeatureDataAccess jdbcMatchFeaturesDataAccess,
      AlertSolvingRepository alertSolvingRepository,
      ReadyMatchFeatureVectorPort readyMatchFeatureVectorPort,
      CommentInputResolveProcess commentInputResolveProcess,
      CategoryResolveProcess categoryResolveProcess) {

    return new SolvingAlertReceivedProcess(
        agentExchnageRequestMapper,
        agentsMatchPublisher,
        jdbcMatchFeaturesDataAccess,
        alertSolvingRepository,
        readyMatchFeatureVectorPort,
        commentInputResolveProcess,
        categoryResolveProcess);
  }

  @Bean
  public CommentInputResolveProcess commentInputResolveProcess(
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClient commentInputClient,
      final CommentInputClientRepository commentInputClientRepository,
      final Queue alertCommentsInputQueue,
      final ProcessConfigurationProperties processConfigurationProperties) {

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
      final Queue alertCategoryValuesQueue,
      ReadyMatchFeatureVectorPort readyMatchFeatureVectorPublisher,
      final ProcessConfigurationProperties processConfigurationProperties) {

    final ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(
            processConfigurationProperties.getCategoryProcess().getPoolSize());

    return new CategoryResolveProcess(
        categoryValueClient,
        scheduledExecutorService,
        alertCategoryValuesQueue,
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
      final ReadyMatchFeatureVectorPort governanceProvider,
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
      final GovernanceAlertPort governancePublisher,
      final ProcessConfigurationProperties processConfigurationProperties) {
    final ExecutorService scheduledExecutorService =
        Executors.newFixedThreadPool(
            processConfigurationProperties.getGovernanceProcess().getPoolSize());
    return new GovernanceMatchResponseProcess(governancePublisher, alertSolvingRepository);
  }
}
