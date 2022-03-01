package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.BANK_IDENTIFICATION_CODES_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Service
@Qualifier("identificationMismatch")
class IdentificationMismatchExtractor implements FeatureExtractor {

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {
    var bankIdentificationCodesFeatureInput =
        extractFeatureInput(etlHit);
    return createFeatureInput(
        BANK_IDENTIFICATION_CODES_FEATURE, bankIdentificationCodesFeatureInput);
  }

  private static BankIdentificationCodesFeatureInput extractFeatureInput(EtlHit etlHit) {
    return BankIdentificationCodesFeatureInput
        .newBuilder()
        .setAlertedPartyMatchingField(etlHit.getMatchedTagContent())
        .setWatchlistMatchingText(StringUtils.join(etlHit.getMatchingTexts(), ", "))
        .addAllWatchlistSearchCodes(etlHit.getSearchCodes())
        .build();
  }
}
