/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.solving.application.process.port.AgentResponsePort;
import com.silenteight.adjudication.engine.solving.application.publisher.MatchFeaturePublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchFeaturePublisherPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class AgentResponseProcessTest {
  private AgentResponsePort agentResponsePort;
  private AlertSolvingRepository alertSolvingRepository;
  private ProtoMessageToObjectNodeConverter converter;
  private MatchFeaturePublisherPort matchFeaturePublisherPort;
  private MatchFeatureStoreDataAccess matchFeatureStoreDataAccess;

  private ReadyMatchFeatureVectorPort readyMatchFeatureVectorPort;

  @BeforeEach
  void setUp() {
    matchFeatureStoreDataAccess = mock(MatchFeatureStoreDataAccess.class);
    readyMatchFeatureVectorPort = mock(ReadyMatchFeatureVectorPort.class);

    alertSolvingRepository = new InMemoryAlertSolvingRepositoryMock();
    converter = new ProtoMessageToObjectNodeConverter(messageRegistryOverwrite());
    matchFeaturePublisherPort =
        new MatchFeaturePublisher(new SimpleAsyncTaskExecutor(),
            matchFeatureStoreDataAccess);
    agentResponsePort =
        new AgentResponseProcess(
            readyMatchFeatureVectorPort,
            alertSolvingRepository,
            converter,
            matchFeaturePublisherPort);
  }

  private MessageRegistry messageRegistryOverwrite() {
    MessageRegistryFactory factory =
        new MessageRegistryFactory(
            "com.silenteight.adjudication.api",
            "com.silenteight.adjudication.internal",
            "com.silenteight.agents",
            "com.google.protobuf",
            "com.google.rpc",
            "com.google.type",
            "com.silenteight.dataretention");

    return factory.create();
  }

  @Test
  public void shouldProcessAgentResponseAndUpdateMatchFeatureValues() {
    alertSolvingRepository.save(
        new AlertSolving(AgentResponseProcessFixture.alertOneAggregateWithTwoMatchesFixture()));
    alertSolvingRepository.save(
        new AlertSolving(AgentResponseProcessFixture.alertTwoAggregateWithOneMatcheFixture()));
    AgentExchangeResponse agentExchangeResponse =
        AgentResponseProcessFixture.agentResponseFixture();
    agentResponsePort.processMatchesFeatureValue(agentExchangeResponse);

    assertThat(fetchMatchFeatureValue(1L, 1L, "features/name")).isEqualTo("NO_DATA");
    assertThat(fetchMatchFeatureValue(1L, 2L, "features/name")).isEqualTo("MATCH");
    assertThat(fetchMatchFeatureValue(2L, 3L, "features/name")).isEqualTo("NO_MATCH");
  }

  private String fetchMatchFeatureValue(long alertId, long matchId, String featureName) {
    return alertSolvingRepository
        .get(alertId)
        .getMatches()
        .get(matchId)
        .getFeatures()
        .get(featureName)
        .getFeatureValue();
  }
}
