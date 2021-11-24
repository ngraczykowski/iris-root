package com.silenteight.payments.bridge.svb.oldetl.service;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertParserServiceTest {

  @ParameterizedTest
  @CsvFileSource(
      resources = "/AlertParserServiceExtractAlertedPartyDataTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void extractAlertedPartyData_parameterizedTest(
      String messageData,
      String fkcoFormat,
      String name,
      String address,
      String ctryTown,
      String nameAddress,
      String accountNumber,
      String hitTag,
      String messageFormat,
      String applicationCode) {

    var alertedMessageData =
        new MessageData(List.of(new MessageTag(hitTag, messageData.replace("\\n", "\n"))));
    var actual = AlertParserService.extractAlertedPartyData(alertedMessageData, hitTag, fkcoFormat,
        applicationCode);
    var expected = AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .name(name)
        .addresses(address != null ? List.of(address) : List.of())
        .ctryTowns(ctryTown != null ? List.of(ctryTown) : List.of())
        .nameAddresses(nameAddress != null ? List.of(nameAddress) : List.of())
        .messageFieldStructure(MessageFieldStructure.valueOf(messageFormat))
        .build();
    assertEquals(expected, actual);
  }
}
