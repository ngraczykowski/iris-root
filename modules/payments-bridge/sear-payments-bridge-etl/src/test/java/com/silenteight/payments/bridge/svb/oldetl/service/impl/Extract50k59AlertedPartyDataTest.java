package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.TAG_50K;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Extract50k59AlertedPartyDataTest {

  Extract50k59AlertedPartyData extract50k59AlertedPartyData;

  @ParameterizedTest
  @CsvFileSource(
      resources = "/Extract50K59AlertedPartyDataTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest(
      String messageData,
      String name,
      String address,
      String ctryTown,
      String nameAddress,
      String accountNumber) {

    MessageData preparedMessageData =
        new MessageData(List.of(new MessageTag(TAG_50K, messageData.replace("\\n", "\n"))));
    extract50k59AlertedPartyData = new Extract50k59AlertedPartyData();

    var actual =
        extract50k59AlertedPartyData.extract(
            new ExtractDisposition(TAG_50K, TAG_50K, preparedMessageData, TAG_50K));

    assertEquals(AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .name(name)
        .addresses(address != null ? List.of(address) : List.of(""))
        .ctryTowns(ctryTown != null ? List.of(ctryTown) : List.of(""))
        .nameAddress(nameAddress)
        .messageFieldStructure(MessageFieldStructure.UNSTRUCTURED)
        .build(), actual);
  }
}
