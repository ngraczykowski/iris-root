package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ExtractReceivAlertedPartyDataTest {

  ExtractReceivbankAlertedPartyData extractReceivbankAlertedPartyData;

  private static final String TAG_RECEIVBANK = "RECEIVBANK";

  @ParameterizedTest
  @CsvFileSource(
      resources = "/ExtractReceivAlertedPartyDataTestCases.csv",
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

    extractReceivbankAlertedPartyData = new ExtractReceivbankAlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_RECEIVBANK, messageData.replace("\\n", "\n")))),
        TAG_RECEIVBANK,
        fkcoFormat);

    var actual =
        extractReceivbankAlertedPartyData.extract(MessageFieldStructure.UNSTRUCTURED);
    assertEquals(AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .name(name)
        .addresses(address != null ? List.of(address) : List.of())
        .ctryTowns(ctryTown != null ? List.of(ctryTown) : List.of())
        .nameAddresses(nameAddress != null ? List.of(nameAddress) : List.of())
        .messageFieldStructure(MessageFieldStructure.UNSTRUCTURED)
        .build(), actual);

  }
}
