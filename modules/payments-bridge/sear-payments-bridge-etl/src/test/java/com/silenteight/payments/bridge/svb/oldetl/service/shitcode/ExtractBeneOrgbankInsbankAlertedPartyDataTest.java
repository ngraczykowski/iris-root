package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractBeneOrgbankInsbankAlertedPartyDataTest {

  ExtractBeneOrgbankInsbankAlertedPartyData extractBeneOrgbankInsbankAlertedPartyData;

  private static final String TAG_BENE = "BENE";

  @ParameterizedTest
  @CsvFileSource(
      resources = "/ExtractBeneOrgbankInsbankAlertedPartyDataTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest(
      String messageData,
      String fkcoFormat,
      String name,
      String address,
      String ctryTown,
      String nameAddress,
      String accountNumber) {

    extractBeneOrgbankInsbankAlertedPartyData = new ExtractBeneOrgbankInsbankAlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_BENE, messageData.replace("\\n", "\n")))),
        TAG_BENE, fkcoFormat);

    var actual =
        extractBeneOrgbankInsbankAlertedPartyData.extract(MessageFieldStructure.UNSTRUCTURED);

    assertEquals(AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .names(name != null ? List.of(name) : List.of(""))
        .addresses(address != null ? List.of(address) : List.of(""))
        .ctryTowns(ctryTown != null ? List.of(ctryTown) : List.of(""))
        .nameAddresses(nameAddress != null ? List.of(nameAddress) : List.of(""))
        .messageFieldStructure(MessageFieldStructure.UNSTRUCTURED)
        .build(), actual);

  }

}
