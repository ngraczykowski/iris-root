package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.TAG_ORIGINATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractOriginatorAlertedPartyDataTest {

  ExtractOriginatorAlertedPartyData extractOriginatorAlertedPartyData;

  @ParameterizedTest
  @CsvFileSource(
      resources = "/ExtractOriginatorAlertedPartyDataTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest(
      String messageData,
      String fkcoFormat,
      String name,
      String address,
      String ctryTown,
      String nameAddress,
      String accountNumber,
      String applicationCode) {

    extractOriginatorAlertedPartyData = new ExtractOriginatorAlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_ORIGINATOR, messageData.replace("\\n", "\n")))));

    var actual =
        extractOriginatorAlertedPartyData.extract(
            MessageFieldStructure.UNSTRUCTURED, fkcoFormat, applicationCode);
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
