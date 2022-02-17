package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getMatchId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationNameAgentEtlProcessTest {

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  private OrganizationNameAgentEtlProcess organizationNameAgentEtlProcess;

  @Mock
  private CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @BeforeEach
  void setup() {
    when(createNameFeatureInputUseCase.createForOrganizationNameAgent(any())).thenReturn(
        NameFeatureInput.newBuilder()
            .setFeature("features/organizationName")
            .addAlertedPartyNames(AlertedPartyName
                .newBuilder()
                .setName("alertedPartyName")
                .build())
            .addWatchlistNames(WatchlistName.newBuilder()
                .setName("watchlistPartyNames")
                .build())
            .build());
    organizationNameAgentEtlProcess =
        new OrganizationNameAgentEtlProcess(createNameFeatureInputUseCase);
  }

  @Test
  void testAgentInputServiceCalled() throws InvalidProtocolBufferException {
    var matchId = getMatchId(1);
    var hitData = createHitData(matchId);
    var dataSourceFeatureInputs =
        organizationNameAgentEtlProcess.createDataSourceFeatureInputs(hitData);

    var featureInput = dataSourceFeatureInputs.get(0);
    var organizationFeatureInput =
        featureInput.getAgentFeatureInput().unpack(NameFeatureInput.class);

    assertThat(featureInput.getFeature()).isEqualTo("features/organizationName");
    assertThat(organizationFeatureInput.getFeature()).isEqualTo("features/organizationName");
    assertThat(organizationFeatureInput.getAlertedPartyNamesCount()).isNotZero();
    assertThat(organizationFeatureInput.getWatchlistNamesCount()).isNotZero();

  }
}
