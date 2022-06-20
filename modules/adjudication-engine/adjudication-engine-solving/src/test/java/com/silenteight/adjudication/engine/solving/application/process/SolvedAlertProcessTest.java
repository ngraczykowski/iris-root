/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;
import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;
import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SolvedAlertProcessTest {

  private SolvedAlertProcess solvedAlertProcess;
  private MockRecommendationPublisher mockRecommendationPublisher;

  @BeforeEach
  void setUp() {
    mockRecommendationPublisher = new MockRecommendationPublisher();
    var dataAccess = new InMemoryMatchFeatureDataAccess();
    var inMemoryAlertSolvingRepositoryMock = new InMemoryAlertSolvingRepositoryMock(dataAccess);

    inMemoryAlertSolvingRepositoryMock.updateMatchSolution(
        1,
        1,
        "solution",
        "{\"reason\":{\"step\": \"steps/b343086d-9a3a-424a-b112-136c6043b98b\", \"policy\": \"policies/63d93105-25cd-41c1-9513-27771fb4bace\", \"features\": [\"features/nameMatchedText\", \"features/historicalRiskAccountNumberFP\", \"features/organizationName2\", \"features/organizationName3\", \"features/name2\", \"features/geo3\", \"features/geo2\", \"features/contextualLearningNameTP\", \"features/nameMatchedText2\", \"features/contextualLearningNameFP\", \"features/historicalRiskAccountNumberTP\", \"features/name\"], \"categories\": [\"categories/messageStructure\", \"categories/matchType\", \"categories/crossmatch\", \"categories/specificTerms2\", \"categories/companyNameSurrounding\", \"categories/specificTerms\", \"categories/watchlistType\"], \"step_title\": \"aaaa\", \"policy_title\": \"FalsePos\", \"feature_vector_signature\": \"jCfYZR7MYgZHBRRIWcyuYpkj0Pk=\"}}");
    inMemoryAlertSolvingRepositoryMock.updateMatchSolution(
        1,
        2,
        "solution",
        "{\"reason\":{\"step\": \"steps/b343086d-9a3a-424a-b112-136c6043b98b\", \"policy\": \"policies/63d93105-25cd-41c1-9513-27771fb4bace\", \"features\": [\"features/nameMatchedText\", \"features/historicalRiskAccountNumberFP\", \"features/organizationName2\", \"features/organizationName3\", \"features/name2\", \"features/geo3\", \"features/geo2\", \"features/contextualLearningNameTP\", \"features/nameMatchedText2\", \"features/contextualLearningNameFP\", \"features/historicalRiskAccountNumberTP\", \"features/name\"], \"categories\": [\"categories/messageStructure\", \"categories/matchType\", \"categories/crossmatch\", \"categories/specificTerms2\", \"categories/companyNameSurrounding\", \"categories/specificTerms\", \"categories/watchlistType\"], \"step_title\": \"aaaa\", \"policy_title\": \"FalsePos\", \"feature_vector_signature\": \"jCfYZR7MYgZHBRRIWcyuYpkj0Pk=\"}}");
    var converter =
        new ProtoMessageToObjectNodeConverter(
            new MessageRegistryFactory(
                    "com.google.api",
                    "com.google.protobuf",
                    "com.google.rpc",
                    "com.google.type",
                    "com.silenteight.dataretention")
                .create());
    var mapper = new AlertSolvingAlertContextMapper(converter);
    var commentFacade = new MockCommentFacadePort();
    var recommendationFacade = new MockRecommendationFacadePort();
    var commentInputRepository = new CommentInputClientRepository(new HashMap<>(), converter);
    var properties = new ProcessConfigurationProperties().getSolvedAlertProcess();
    solvedAlertProcess =
        new SolvedAlertProcess(
            mockRecommendationPublisher,
            inMemoryAlertSolvingRepositoryMock,
            mapper,
            commentFacade,
            recommendationFacade,
            converter,
            commentInputRepository,
            properties);
  }

  @Test
  void shouldNotSendRecommendation() {
    solvedAlertProcess.generateRecommendation(1L, BatchSolveAlertsResponse.newBuilder().build());
    assertThat(mockRecommendationPublisher.getRecommendationCount()).isEqualTo(0);
  }

  @Test
  void shouldSendRecommendation() {
    var solvedAlertResponse =
        SolveAlertSolutionResponse.newBuilder()
            .setAlertName("alerts/1")
            .setAlertSolution("SOLVED")
            .build();
    var batchSolvedAlertResponse =
        BatchSolveAlertsResponse.newBuilder().addSolutions(solvedAlertResponse).build();

    solvedAlertProcess.generateRecommendation(1L, batchSolvedAlertResponse);

    assertThat(mockRecommendationPublisher.getRecommendationCount()).isEqualTo(1);
  }
}
