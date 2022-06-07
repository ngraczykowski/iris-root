/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.AgentsMatchPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SolvingAlertReceivedProcessTest {

  private SolvingAlertReceivedProcess solvingAlertReceivedProcess;
  private AgentsMatchPort agentsMatchPort;
  private MatchFeatureDataAccess matchFeatureDataAccess;
  private AlertSolvingRepository alertSolvingRepository;
  private ReadyMatchFeatureVectorPortMock readyMatchFeatureVectorPort;
  private CommentInputResolveProcessMock commentInputResolveProcessPort;
  private CategoryResolveProcessPortMock categoryResolveProcess;

  @BeforeEach
  void setUp() {
    var mapper = new AgentExchangeAlertSolvingMapper();
    agentsMatchPort = new AgentsMatchPortMock();
    matchFeatureDataAccess = new InMemoryMatchFeatureDataAccess();
    alertSolvingRepository = new InMemoryAlertSolvingRepositoryMock();

    readyMatchFeatureVectorPort = new ReadyMatchFeatureVectorPortMock();
    commentInputResolveProcessPort = new CommentInputResolveProcessMock();
    categoryResolveProcess = new CategoryResolveProcessPortMock();

    solvingAlertReceivedProcess =
        new SolvingAlertReceivedProcess(
            mapper,
            agentsMatchPort,
            matchFeatureDataAccess,
            alertSolvingRepository,
            readyMatchFeatureVectorPort,
            commentInputResolveProcessPort,
            categoryResolveProcess);
  }

  @Test
  void shouldSolveMatchesWithReadyFeatureVector() {
    solvingAlertReceivedProcess.handle(
        AnalysisAlertsAdded.newBuilder().addAnalysisAlerts("analysis/1/alerts/1").build());

    assertThat(readyMatchFeatureVectorPort.getRequestsCount()).isEqualTo(2);
    assertThat(commentInputResolveProcessPort.getAlertsCount()).isEqualTo(1);
    assertThat(categoryResolveProcess.getAlertsCount()).isEqualTo(1);
  }

  @Test
  void shouldSendRequestForFeatureValuesAndCategories() {
    solvingAlertReceivedProcess.handle(
        AnalysisAlertsAdded.newBuilder().addAnalysisAlerts("analysis/1/alerts/2").build());

    assertThat(readyMatchFeatureVectorPort.getRequestsCount()).isEqualTo(0);
    assertThat(commentInputResolveProcessPort.getAlertsCount()).isEqualTo(1);
    assertThat(categoryResolveProcess.getAlertsCount()).isEqualTo(1);
  }
}
