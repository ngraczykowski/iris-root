package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createAeAlert;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createAlertEtlResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameAgentEtlProcessTest {

  private NameAgentEtlProcess nameAgentEtlProcess;
  @Mock
  private AgentInputServiceBlockingStub stub;


  @BeforeEach
  void setup() {
    Duration timeout = Duration.ofMillis(500L);
    when(stub.batchCreateAgentInputs(any())).thenReturn(
        BatchCreateAgentInputsResponse.newBuilder().build());
    when(stub.withDeadline(any())).thenReturn(stub);
    nameAgentEtlProcess = new NameAgentEtlProcess(stub, timeout);
  }

  @Test
  void testAgentInputServiceCalled() {
    int numberOfMatches = 5;
    nameAgentEtlProcess.extractAndLoad(
        createAeAlert(numberOfMatches), createAlertEtlResponse(numberOfMatches));
    verify(stub, times(numberOfMatches)).batchCreateAgentInputs(any());
  }
}
