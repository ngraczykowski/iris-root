package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;

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
  @Mock
  private CreateNameFeatureInputUseCase nameAgentUseCase;


  @BeforeEach
  void setup() {
    Duration timeout = Duration.ofMillis(500L);
    when(stub.batchCreateAgentInputs(any())).thenReturn(
        BatchCreateAgentInputsResponse.newBuilder().build());
    when(stub.withDeadline(any())).thenReturn(stub);
    when(nameAgentUseCase.create(any())).thenReturn(NameFeatureInput.newBuilder().build());
    nameAgentEtlProcess = new NameAgentEtlProcess(stub, timeout, nameAgentUseCase);
  }

  @Test
  void testAgentInputServiceCalled() {
    int numberOfMatches = 5;
    nameAgentEtlProcess.extractAndLoad(
        createAeAlert(numberOfMatches), createAlertEtlResponse(numberOfMatches));
    verify(stub, times(numberOfMatches)).batchCreateAgentInputs(any());
  }
}
