package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.TAG_ORIGINATOR;
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

    MessageData preparedMessageData =
        new MessageData(List.of(new MessageTag(TAG_ORIGINATOR, messageData.replace("\\n", "\n"))));
    extractOriginatorAlertedPartyData = new ExtractOriginatorAlertedPartyData();

    var actual =
        extractOriginatorAlertedPartyData.extract(
            new ExtractDisposition(
                applicationCode, fkcoFormat, preparedMessageData, TAG_ORIGINATOR));
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
