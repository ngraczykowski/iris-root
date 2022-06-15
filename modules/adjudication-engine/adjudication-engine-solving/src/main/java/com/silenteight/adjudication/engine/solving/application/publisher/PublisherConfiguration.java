package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.port.CategoryResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.process.port.CommentInputResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.process.port.GovernanceMatchResponsePort;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvedAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.*;
import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureValue;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(PublisherConfigurationProperties.class)
@RequiredArgsConstructor
class PublisherConfiguration {
  private final PublisherConfigurationProperties properties;

  @Bean
  ReadyMatchFeatureVectorPort readyMatchFeatureVectorPort(
      GovernanceFacade governanceFacade,
      Queue<MatchSolutionRequest> governanceMatchToSendQueue,
      GovernanceMatchResponsePort governanceMatchResponseProcess,
      ProtoMessageToObjectNodeConverter converter) {
    ExecutorService scheduledExecutorService =
        Executors.newFixedThreadPool(properties.getReadyMatchFeatureVectorPublisher());
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
      Queue<AlertSolutionRequest> governanceAlertsToSendQueue,
      SolvedAlertPort solvedAlertProcess) {
    ExecutorService scheduledExecutorService =
        Executors.newFixedThreadPool(properties.getGovernanceAlertPublisher());
    return new GovernanceAlertPublisher(
        governanceFacade,
        governanceAlertsToSendQueue,
        scheduledExecutorService,
        solvedAlertProcess);
  }

  @Bean
  CategoryResolvePublisher categoryResolvePublisher(
      Queue<Long> alertCategoryValuesQueue, CategoryResolveProcessPort categoryResolveProcess) {
    var scheduledExecutorService =
        Executors.newScheduledThreadPool(properties.getCategoryResolvePublisher());
    return new CategoryResolvePublisher(
        scheduledExecutorService, alertCategoryValuesQueue, categoryResolveProcess);
  }

  @Bean
  CommentInputResolvePublisherPort commentInputResolvePublisher(
      CommentInputResolveProcessPort commentInputResolveProcessPort,
      Queue<String> alertCommentsInputQueue) {
    var scheduledExecutorService =
        Executors.newScheduledThreadPool(properties.getCommentInputResolvePublisher());
    return new CommentInputResolvePublisher(
        commentInputResolveProcessPort, scheduledExecutorService, alertCommentsInputQueue);
  }

  @Bean
  CommentInputStorePublisherPort commentInputStorePublisherPort(
      CommentInputDataAccess commentInputDataAccess,
      Queue<CommentInput> alertCommentsInputStoreQueue) {
    var scheduledExecutorService =
        Executors.newScheduledThreadPool(properties.getCommentInputResolvePublisher());
    return new CommentInputStorePublisher(
        commentInputDataAccess, scheduledExecutorService, alertCommentsInputStoreQueue);
  }

  @Bean
  MatchCategoryPublisherPort matchCategoryPublisherPort(
      MatchCategoryDataAccess matchCategoryDataAccess,
      Queue<MatchCategory> matchCategoryStoreQueue) {
    var scheduledExecutorService =
        Executors.newScheduledThreadPool(properties.getMatchCategoryPublisher());
    return new MatchCategoryPublisher(
        matchCategoryDataAccess, scheduledExecutorService, matchCategoryStoreQueue);
  }

  @Bean
  MatchFeaturePublisherPort matchFeaturePublisherPort(
      MatchFeatureStoreDataAccess matchFeatureStoreDataAccess,
      Queue<MatchFeatureValue> matchFeatureStoreQueue) {
    var scheduledExecutorService =
        Executors.newScheduledThreadPool(properties.getMatchFeaturePublisher());
    return new MatchFeaturePublisher(
        matchFeatureStoreDataAccess, scheduledExecutorService, matchFeatureStoreQueue);
  }

  @Bean
  MatchSolutionPublisherPort matchSolutionPublisherPort(
      MatchSolutionStore matchSolutionStore,
      Queue<MatchSolution> matchSolutionStoreQueue) {
    var scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    return new MatchSolutionPublisher(
        matchSolutionStore, scheduledExecutorService, matchSolutionStoreQueue);
  }
}
