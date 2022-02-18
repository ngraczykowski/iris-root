package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.agents.model.BankIdentificationCodesAgentsRequest;
import com.silenteight.payments.bridge.agents.port.CreateBankIdentificationCodesFeatureInputUseCase;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateBankIdentificationCodesFeatureInputTest {

  private CreateBankIdentificationCodesFeatureInputUseCase
      createBankIdentificationCodesFeatureInputUseCase;

  @BeforeEach
  void beforeEach() {
    createBankIdentificationCodesFeatureInputUseCase =
        new CreateBankIdentificationCodesFeatureInput();
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CreateBankIdentificationCodesFeatureInputUseCaseTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parametrizedTest(
      String feature,
      String fieldValue,
      String matchingText,
      String solutionType,
      String searchCodes,
      String passports,
      String natIds,
      String bics,
      String expectedFeature,
      String expectedAlertedPartyMatchingField,
      String expectedWatchlistMatchingText,
      String expectedWatchlistSearchCodes
  ) {

    var actualBankIdentificationCodesFeatureInput =
        createBankIdentificationCodesFeatureInputUseCase.create(
            BankIdentificationCodesAgentsRequest.builder()
                .feature(feature)
                .fieldValue(fieldValue)
                .matchingText(matchingText)
                .solutionType(SolutionType.valueOf(solutionType))
                .searchCodes(List.of(searchCodes))
                .passports(List.of(passports))
                .natIds(List.of(natIds))
                .bics(List.of(bics))
                .build()
        );

    var expectedLocationFeatureInput = BankIdentificationCodesFeatureInput.newBuilder()
        .setFeature(expectedFeature)
        .setAlertedPartyMatchingField(expectedAlertedPartyMatchingField)
        .setWatchlistMatchingText(expectedWatchlistMatchingText);

    if (StringUtils.isNotBlank(expectedWatchlistSearchCodes)) {
      expectedLocationFeatureInput.addAllWatchlistSearchCodes(
          List.of(expectedWatchlistSearchCodes));
    }

    assertEquals(expectedLocationFeatureInput.build(), actualBankIdentificationCodesFeatureInput);
  }
}
