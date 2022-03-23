package com.silenteight.payments.bridge.datasource.agent;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.IdentificationMismatchAgentData;

import com.google.protobuf.Any;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.BANK_IDENTIFICATION_CODES_FEATURE_NAME;

@Component
class IdentificationMismatchAgentFactory extends BaseFeatureInputStructuredFactory {

  @Override
  protected FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured) {

    var identificationMismatchAgentData =
        featureInputStructured.getIdentificationMismatchAgentData();
    var bankIdentificationFeatureInput =
        createBankIdentificationFeatureInput(identificationMismatchAgentData);

    return FeatureInput
        .newBuilder()
        .setFeature(BANK_IDENTIFICATION_CODES_FEATURE_NAME)
        .setAgentFeatureInput(Any.pack(bankIdentificationFeatureInput))
        .build();
  }

  @Override
  protected String getFeatureName() {
    return BANK_IDENTIFICATION_CODES_FEATURE_NAME;
  }

  private static BankIdentificationCodesFeatureInput createBankIdentificationFeatureInput(
      IdentificationMismatchAgentData identificationMismatchAgentData) {
    return BankIdentificationCodesFeatureInput
        .newBuilder()
        .setFeature(BANK_IDENTIFICATION_CODES_FEATURE_NAME)
        .setAlertedPartyMatchingField(
            identificationMismatchAgentData.getAlertedPartyMatchingField())
        .setWatchlistMatchingText(identificationMismatchAgentData.getMatchingText())
        .addAllWatchlistSearchCodes(identificationMismatchAgentData.getWatchlistSearchCodes())
        .build();
  }
}
