package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getMatchId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameAgentEtlProcessTest {

  private NameAgentEtlProcess nameAgentEtlProcess;

  @Mock
  private CreateNameFeatureInputUseCase nameAgentUseCase;

  @BeforeEach
  void setup() {
    when(nameAgentUseCase.create(any())).thenReturn(NameFeatureInput.newBuilder()
        .setFeature("features/name")
        .addAlertedPartyNames(AlertedPartyName
            .newBuilder()
            .setName("alertedPartyName")
            .build())
        .addWatchlistNames(WatchlistName.newBuilder()
            .setName("watchlistPartyNames")
            .build())
        .build());
    nameAgentEtlProcess = new NameAgentEtlProcess(nameAgentUseCase);
  }

  @Test
  void testAgentInputServiceCalled() throws InvalidProtocolBufferException {
    var matchId = getMatchId(1);
    var hitData = createHitData(matchId);
    var dataSourceFeatureInputs =
        nameAgentEtlProcess.createDataSourceFeatureInputs(hitData);

    var featureInput = dataSourceFeatureInputs.get(0);
    var organizationFeatureInput =
        featureInput.getAgentFeatureInput().unpack(NameFeatureInput.class);

    assertThat(featureInput.getFeature()).isEqualTo("features/name");
    assertThat(organizationFeatureInput.getFeature()).isEqualTo("features/name");

    assertThat(organizationFeatureInput.getAlertedPartyNamesList().stream()
        .filter(ap -> ap.getName().equals("alertedPartyName"))
        .count()).isOne();

    assertThat(organizationFeatureInput.getWatchlistNamesList().stream()
        .filter(ap -> ap.getName().equals("watchlistPartyNames"))
        .count()).isOne();

  }
}
