package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Extract50k59AlertedPartyDataTest {

  @Test
  void shouldExtractValues() {
    var extract50k59AlertedPartyData = new Extract50k59AlertedPartyData(
        new MessageData(List.of(new MessageTag("50K", "/acctnum\n"
            + "name\n"
            + "address\n"
            + "address\n"
            + "ctrytown"))));
    assertThat(extract50k59AlertedPartyData.extract(
        "50K", MessageFieldStructure.NAMEADDRESS_FORMAT_F)).isEqualTo(
        AlertedPartyData.builder()
            .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_F)
            .accountNumber("acctnum")
            .name("name")
            .addresses(List.of("address", "address"))
            .nameAddresses(List.of("name", "address", "address"))
            .ctryTown("ctrytown")
            .build());
  }
}
