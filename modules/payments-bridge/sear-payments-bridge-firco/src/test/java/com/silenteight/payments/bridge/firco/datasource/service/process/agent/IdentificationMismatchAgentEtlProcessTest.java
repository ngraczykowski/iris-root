package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.agents.port.CreateBankIdentificationCodesFeatureInputUseCase;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.createHitData;
import static com.silenteight.payments.bridge.firco.datasource.service.process.EtlProcessFixture.getMatchId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentificationMismatchAgentEtlProcessTest {

  private IdentificationMismatchAgentEtlProcess
      identificationMismatchAgentEtlProcess;

  @Mock
  private CreateBankIdentificationCodesFeatureInputUseCase
      createBankIdentificationCodesFeatureInputUseCase;

  @BeforeEach
  void setup() {
    when(createBankIdentificationCodesFeatureInputUseCase.create(any())).thenReturn(
        BankIdentificationCodesFeatureInput.newBuilder()
            .setFeature("features/bankIdentificationCodes")
            .setAlertedPartyMatchingField("fieldValue")
            .setWatchlistMatchingText("matchingText")
            .addAllWatchlistSearchCodes(List.of("123123123", "1231231231"))
            .build());
    identificationMismatchAgentEtlProcess =
        new IdentificationMismatchAgentEtlProcess(createBankIdentificationCodesFeatureInputUseCase);
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
