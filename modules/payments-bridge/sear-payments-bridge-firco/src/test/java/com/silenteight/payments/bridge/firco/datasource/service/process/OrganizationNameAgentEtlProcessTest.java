package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createAeAlert;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessHelper.createAlertEtlResponse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationNameAgentEtlProcessTest {

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  private OrganizationNameAgentEtlProcess organizationNameAgentEtlProcess;

  @Mock
  private AgentInputServiceBlockingStub stub;


  @BeforeEach
  void setup() {
    Duration timeout = Duration.ofMillis(500L);
    when(stub.batchCreateAgentInputs(any())).thenReturn(
        BatchCreateAgentInputsResponse.newBuilder().build());
    when(stub.withDeadline(any())).thenReturn(stub);
    organizationNameAgentEtlProcess = new OrganizationNameAgentEtlProcess(stub, timeout);
  }

  @Test
  void testAgentInputServiceCalled() {
    int numberOfMatches = 5;
    organizationNameAgentEtlProcess.extractAndLoad(
        createAeAlert(numberOfMatches), createAlertEtlResponse(numberOfMatches));
    verify(stub, times(numberOfMatches)).batchCreateAgentInputs(any());
  }
}
