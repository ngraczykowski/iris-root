package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.ExtractDisposition;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.TAG_ORIGINATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractOriginatorBeneFormatFAlertedPartyDataTest {

  ExtractOriginatorBeneFormatFAlertedPartyData extractOriginatorBeneFormatFAlertedPartyData;

  @ParameterizedTest
  @CsvFileSource(
      resources = "/ExtractOriginatorBeneFormatFAlertedPartyDataTestCases.csv",
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
        new MessageData(List.of(new MessageTag(TAG_ORIGINATOR, messageData.replace("\\n", "\n"))));
    extractOriginatorBeneFormatFAlertedPartyData =
        new ExtractOriginatorBeneFormatFAlertedPartyData();

    var actual = extractOriginatorBeneFormatFAlertedPartyData.extract(
        new ExtractDisposition(null, TAG_ORIGINATOR,
            preparedMessageData, TAG_ORIGINATOR));
    assertEquals(AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .name(name)
        .address(address)
        .ctryTown(ctryTown)
        .nameAddress(nameAddress)
        .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_F)
        .build(), actual);

  }
}
