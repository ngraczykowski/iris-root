/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.process.dto.MatchSolutionResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GovernanceMatchResponseProcessTest {

  private GovernanceMatchResponseProcess governanceMatchResponseProcess;
  private MockGovernanceAlertPort mockGovernanceAlertPort;
  private InMemoryAlertSolvingRepositoryMock inMemoryAlertSolvingRepositoryMock;

  @BeforeEach
  void setUp() {
    mockGovernanceAlertPort = new MockGovernanceAlertPort();
    var dataAccess = new InMemoryMatchFeatureDataAccess();
    inMemoryAlertSolvingRepositoryMock = new InMemoryAlertSolvingRepositoryMock(dataAccess);

    governanceMatchResponseProcess =
        new GovernanceMatchResponseProcess(
            mockGovernanceAlertPort, inMemoryAlertSolvingRepositoryMock);
  }

  @Test
  void shouldSendReadyAlertForSolving() {
    var request = new MatchSolutionResponse(1L, 1L, "solution", "reason");
    var request2 = new MatchSolutionResponse(1L, 2L, "solution", "reason");
    governanceMatchResponseProcess.processAlert(request);
    governanceMatchResponseProcess.processAlert(request2);
    assertThat(mockGovernanceAlertPort.getRequestsCount()).isEqualTo(1);
  }

  @Test
  void shouldNotSendNotReadyAlertForSolving() {
    var request = new MatchSolutionResponse(1L, 1L, "solution", "reason");
    governanceMatchResponseProcess.processAlert(request);
    assertThat(mockGovernanceAlertPort.getRequestsCount()).isEqualTo(0);
  }
}
