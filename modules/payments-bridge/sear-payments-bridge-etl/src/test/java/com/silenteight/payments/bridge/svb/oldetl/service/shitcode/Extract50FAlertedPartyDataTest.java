package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Extract50FAlertedPartyDataTest {

  Extract50FAlertedPartyData extract50FAlertedPartyData;

  private static final String TAG_50F = "50F";

  @ParameterizedTest
  @CsvFileSource(
      resources = "/Extract50FAlertedPartyDataTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest(
      String messageData,
      String name,
      String address,
      String ctryTown,
      String nameAddress,
      String accountNumber) {

    extract50FAlertedPartyData = new Extract50FAlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_50F, messageData.replace("\\n", "\n")))),
        TAG_50F);

    var actual = extract50FAlertedPartyData.extract(MessageFieldStructure.NAMEADDRESS_FORMAT_F);
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
