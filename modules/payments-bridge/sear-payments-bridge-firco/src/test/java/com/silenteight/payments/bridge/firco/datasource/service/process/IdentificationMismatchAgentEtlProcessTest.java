package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createAlertEtlResponse;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createAlertRegisteredEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentificationMismatchAgentEtlProcessTest {

  private IdentificationMismatchAgentEtlProcess
      identificationMismatchAgentEtlProcess;
  @Mock
  private AgentInputServiceBlockingStub stub;

  @BeforeEach
  void setup() {
    Duration timeout = Duration.ofMillis(500L);
    when(stub.batchCreateAgentInputs(any())).thenReturn(
        BatchCreateAgentInputsResponse.newBuilder().build());
    when(stub.withDeadline(any())).thenReturn(stub);
    identificationMismatchAgentEtlProcess =
        new IdentificationMismatchAgentEtlProcess(stub, timeout);
  }

  @Test
  void testAgentInputServiceCalled() {
    int numberOfMatches = 5;
    identificationMismatchAgentEtlProcess.extractAndLoad(
        createAlertRegisteredEvent(numberOfMatches), createAlertEtlResponse(numberOfMatches));
    verify(stub, times(numberOfMatches)).batchCreateAgentInputs(any());
  }
}
