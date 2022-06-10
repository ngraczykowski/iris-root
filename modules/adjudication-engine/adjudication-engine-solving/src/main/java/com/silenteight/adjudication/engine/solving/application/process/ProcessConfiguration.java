package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryValuesClient;
import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFacade;
import com.silenteight.adjudication.engine.comments.comment.CommentFacade;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.port.*;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
      CommentInputResolvePublisherPort commentInputResolveProcess,
      CategoryResolvePublisherPort categoryResolvePublisher) {

    return new SolvingAlertReceivedProcess(
        agentExchnageRequestMapper,
        agentsMatchPublisher,
        jdbcMatchFeaturesDataAccess,
        alertSolvingRepository,
        readyMatchFeatureVectorPort,
        commentInputResolveProcess,
        categoryResolvePublisher);
  }

  @Bean
  public CommentInputResolveProcess commentInputResolveProcess(
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClient commentInputClient,
      final CommentInputClientRepository commentInputClientRepository,
      final CommentInputStorePublisherPort commentInputStorePublisherPort) {

    return new CommentInputResolveProcess(
        commentInputClient,
        converter,
        commentInputClientRepository,
        commentInputStorePublisherPort);
  }

  @Bean
  public CategoryResolveProcess categoryResolveProcess(
      CategoryValuesClient categoryValueClient,
      AlertSolvingRepository alertSolvingRepository,
      ReadyMatchFeatureVectorPort readyMatchFeatureVectorPublisher,
      MatchCategoryPublisherPort matchCategoryPublisherPort) {

    return new CategoryResolveProcess(
        categoryValueClient,
        alertSolvingRepository,
        readyMatchFeatureVectorPublisher,
        matchCategoryPublisherPort);
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
      ProtoMessageToObjectNodeConverter converter,
      MatchFeaturePublisherPort matchFeaturePublisherPort) {
    return new AgentResponseProcess(
        governanceProvider, alertSolvingRepository, converter, matchFeaturePublisherPort);
  }

  @Bean
  GovernanceMatchResponseProcess governanceMatchResponseProcess(
      final AlertSolvingRepository alertSolvingRepository,
      final GovernanceAlertPort governancePublisher,
      final ProcessConfigurationProperties processConfigurationProperties) {
    return new GovernanceMatchResponseProcess(governancePublisher, alertSolvingRepository);
  }
}
