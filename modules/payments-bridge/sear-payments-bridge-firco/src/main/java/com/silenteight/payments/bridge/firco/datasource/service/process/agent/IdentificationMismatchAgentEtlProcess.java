package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.agents.model.BankIdentificationCodesAgentsRequest;
import com.silenteight.payments.bridge.agents.port.CreateBankIdentificationCodesFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.BANK_IDENTIFICATION_CODES_FEATURE;

@Component
@RequiredArgsConstructor
class IdentificationMismatchAgentEtlProcess
    extends BaseAgentEtlProcess<BankIdentificationCodesFeatureInput> {

  private final CreateBankIdentificationCodesFeatureInputUseCase
      createBankIdentificationCodesFeatureInputUseCase;

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var featureInput = FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(BANK_IDENTIFICATION_CODES_FEATURE))
        .setAgentFeatureInput(Any.pack(createBankIdentificationFeatureInput(hitData)))
        .build();

    return List.of(featureInput);
  }

  private BankIdentificationCodesFeatureInput createBankIdentificationFeatureInput(
      HitData hitData) {

    var bankIdentificationCodesAgentsRequest = BankIdentificationCodesAgentsRequest.builder()
        .feature(BANK_IDENTIFICATION_CODES_FEATURE)
        .fieldValue(hitData.getFieldValue())
        .matchingText(hitData.getMatchingText())
        .solutionType(hitData.getSolutionType())
        .searchCodes(hitData.getSearchCodes())
        .passports(hitData.getPassports())
        .natIds(hitData.getNatIds())
        .bics(hitData.getBics())
        .build();

    return createBankIdentificationCodesFeatureInputUseCase.create(
        bankIdentificationCodesAgentsRequest);
  }
}
