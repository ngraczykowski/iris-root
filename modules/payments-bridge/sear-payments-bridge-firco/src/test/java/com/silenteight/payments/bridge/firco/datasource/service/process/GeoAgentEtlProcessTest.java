package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.datasource.port.CreateAgentInputsClient;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getMatchId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class GeoAgentEtlProcessTest {

  private GeoAgentEtlProcess geoAgentEtlProcess;

  @Mock
  private CreateAgentInputsClient createAgentInputsClient;

  @BeforeEach
  void setup() {
    geoAgentEtlProcess = new GeoAgentEtlProcess(createAgentInputsClient);
  }

  @Test
  void testAgentInputServiceCalled() throws InvalidProtocolBufferException {
    var matchId = getMatchId(1);
    var hitData = createHitData(matchId);
    var dataSourceFeatureInputs =
        geoAgentEtlProcess.createDataSourceFeatureInputs(hitData);

    var featureInput = dataSourceFeatureInputs.get(0);
    var locationFeatureInput =
        featureInput.getAgentFeatureInput().unpack(LocationFeatureInput.class);

    assertThat(featureInput.getFeature()).isEqualTo("features/geo");
    assertThat(locationFeatureInput.getFeature()).isEqualTo("features/geo");

    assertThat(locationFeatureInput.getWatchlistLocation()).isNotBlank();
    assertThat(locationFeatureInput.getAlertedPartyLocation()).isEmpty();
  }
}
