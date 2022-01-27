package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getMatchId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class IdentificationMismatchAgentEtlProcessTest {

  private IdentificationMismatchAgentEtlProcess
      identificationMismatchAgentEtlProcess;

  @BeforeEach
  void setup() {
    identificationMismatchAgentEtlProcess =
        new IdentificationMismatchAgentEtlProcess();
  }

  @Test
  void testAgentInputServiceCalled() throws InvalidProtocolBufferException {
    var matchId = getMatchId(1);
    var hitData = createHitData(matchId);
    var dataSourceFeatureInputs =
        identificationMismatchAgentEtlProcess.createDataSourceFeatureInputs(hitData);

    var featureInput = dataSourceFeatureInputs.get(0);
    var bankIdentificationCodesFeatureInput =
        featureInput.getAgentFeatureInput().unpack(BankIdentificationCodesFeatureInput.class);

    assertThat(featureInput.getFeature()).isEqualTo("features/bankIdentificationCodes");
    assertThat(bankIdentificationCodesFeatureInput.getFeature()).isEqualTo(
        "features/bankIdentificationCodes");

    assertThat(bankIdentificationCodesFeatureInput.getAlertedPartyMatchingField()).isEqualTo(
        "fieldValue");
    assertThat(bankIdentificationCodesFeatureInput.getWatchlistMatchingText()).isEqualTo(
        "matchingText");
    assertThat(bankIdentificationCodesFeatureInput.getWatchlistSearchCodesCount()).isEqualTo(2);
  }
}
