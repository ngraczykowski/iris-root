package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.agents.model.BankIdentificationCodesAgentsRequest;
import com.silenteight.payments.bridge.agents.port.CreateBankIdentificationCodesFeatureInputUseCase;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
class CreateBankIdentificationCodesFeatureInput implements
    CreateBankIdentificationCodesFeatureInputUseCase {

  @Override
  public BankIdentificationCodesFeatureInput create(BankIdentificationCodesAgentsRequest request) {

    return BankIdentificationCodesFeatureInput
        .newBuilder()
        .setFeature(request.getFeature())
        .setAlertedPartyMatchingField(request.getFieldValue())
        .setWatchlistMatchingText(request.getMatchingText())
        .addAllWatchlistSearchCodes(setWatchlistSearchCodes(request))
        .build();
  }

  private static List<String> setWatchlistSearchCodes(
      BankIdentificationCodesAgentsRequest request) {

    var solutionType = request.getSolutionType();

    if (SolutionType.SEARCH_CODE == solutionType) {
      return request.getSearchCodes();
    } else if (SolutionType.PASSPORT == solutionType) {
      return request.getPassports();
    } else if (SolutionType.NATIONAL_ID == solutionType) {
      return request.getNatIds();
    } else if (SolutionType.BIC == solutionType) {
      return request.getBics();
    }

    return Collections.emptyList();
  }
}
