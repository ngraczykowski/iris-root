package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.adjudication.engine.solving.application.process.port.CategoryResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.process.port.CommentInputResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.process.port.GovernanceMatchResponsePort;
import com.silenteight.adjudication.engine.solving.application.process.port.SolvedAlertPort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.AlertSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputResolvePublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputStorePublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchCategoryPublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

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
      Queue<MatchSolutionRequest> governanceMatchToSendQueue,
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
      Queue<AlertSolutionRequest> governanceAlertsToSendQueue,
      SolvedAlertPort solvedAlertProcess) {
    final ExecutorService scheduledExecutorService = Executors.newFixedThreadPool(15);
    return new GovernanceAlertPublisher(
        governanceFacade,
        governanceAlertsToSendQueue,
        scheduledExecutorService,
        solvedAlertProcess);
  }

  @Bean
  CategoryResolvePublisher categoryResolvePublisher(
      Queue<Long> alertCategoryValuesQueue, CategoryResolveProcessPort categoryResolveProcess) {
    var scheduledExecutorService = Executors.newScheduledThreadPool(15);
    return new CategoryResolvePublisher(
        scheduledExecutorService, alertCategoryValuesQueue, categoryResolveProcess);
  }

  @Bean
  CommentInputResolvePublisherPort commentInputResolvePublisher(
      CommentInputResolveProcessPort commentInputResolveProcessPort,
      Queue<String> alertCommentsInputQueue) {
    var scheduledExecutorService = Executors.newScheduledThreadPool(15);
    return new CommentInputResolvePublisher(
        commentInputResolveProcessPort, scheduledExecutorService, alertCommentsInputQueue);
  }

  @Bean
  CommentInputStorePublisherPort commentInputStorePublisherPort(
      CommentInputDataAccess commentInputDataAccess,
      Queue<CommentInput> alertCommentsInputStoreQueue) {
    var scheduledExecutorService = Executors.newScheduledThreadPool(1);
    return new CommentInputStorePublisher(
        commentInputDataAccess, scheduledExecutorService, alertCommentsInputStoreQueue);
  }

  @Bean
  MatchCategoryPublisherPort matchCategoryPublisherPort(
      MatchCategoryDataAccess matchCategoryDataAccess,
      Queue<MatchCategory> matchCategoryStoreQueue) {
    var scheduledExecutorService = Executors.newScheduledThreadPool(1);
    return new MatchCategoryPublisher(
        matchCategoryDataAccess, scheduledExecutorService, matchCategoryStoreQueue);
  }
}
