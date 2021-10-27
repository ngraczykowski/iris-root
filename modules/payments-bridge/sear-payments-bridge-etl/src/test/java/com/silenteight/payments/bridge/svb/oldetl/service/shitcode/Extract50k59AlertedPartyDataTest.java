package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Extract50k59AlertedPartyDataTest {

  Extract50k59AlertedPartyData extract50k59AlertedPartyData;

  private static final String TAG_50K = "50K";
  private static final String TAG_59 = "59";

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

    extract50k59AlertedPartyData = new Extract50k59AlertedPartyData(
        new MessageData(List.of(new MessageTag(TAG_50K, messageData.replace("\\n", "\n")))));

    var actual = extract50k59AlertedPartyData.extract(TAG_50K,
        MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED);

    assertEquals(AlertedPartyData.builder()
        .accountNumber(accountNumber)
        .name(name)
        .addresses(address != null ? List.of(address) : List.of(""))
        .ctryTowns(ctryTown != null ? List.of(ctryTown) : List.of(""))
        .nameAddress(nameAddress)
        .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED)
        .build(), actual);
  }
}
