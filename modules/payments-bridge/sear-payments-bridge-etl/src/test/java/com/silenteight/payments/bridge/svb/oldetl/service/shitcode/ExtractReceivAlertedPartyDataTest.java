package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
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
        .address(address)
        .ctryTown(ctryTown)
        .nameAddress(nameAddress)
        .messageFieldStructure(MessageFieldStructure.UNSTRUCTURED)
        .build(), actual);

  }
}
