package com.silenteight.payments.bridge.svb.oldetl.service.impl;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractOriginatorBeneFormatFAlertedPartyDataTest {

  ExtractOriginatorBeneFormatFAlertedPartyData extractOriginatorBeneFormatFAlertedPartyData;

  private static final String TAG_ORIGINATOR = "ORIGINATOR";

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

    extractOriginatorBeneFormatFAlertedPartyData = new ExtractOriginatorBeneFormatFAlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_ORIGINATOR, messageData.replace("\\n", "\n")))),
        TAG_ORIGINATOR);

    var actual = extractOriginatorBeneFormatFAlertedPartyData.extract(
        MessageFieldStructure.NAMEADDRESS_FORMAT_F);
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
