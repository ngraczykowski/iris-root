package com.silenteight.payments.bridge.svb.oldetl.service.shitcode;

import com.silenteight.payments.bridge.etl.firco.parser.MessageFormat;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExtractOriginatorAlertedPartyDataTest {

  private ExtractOriginatorAlertedPartyData extractOriginatorAlertedPartyData;

  @Test
  void shouldExtractFirstLineLengthTwo() {
    extractOriginatorAlertedPartyData = new ExtractOriginatorAlertedPartyData(
        new MessageData(List.of(new MessageTag("ORIGINATOR", "AC\n"
            + "NL60SNSB0907370640\n"
            + "SADDAM HUSSEIN\n"
            + "2/BREED 9\n"
            + "3/NL/1621KA HOORN NH\n"
            + "US"))));
    assertThat(extractOriginatorAlertedPartyData.extract(
        MessageFieldStructure.NAMEADDRESS_FORMAT_F, MessageFormat.IATI)).isEqualTo(
        AlertedPartyData.builder()
            .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_F)
            .accountNumber("NL60SNSB0907370640")
            .name("SADDAM HUSSEIN")
            .addresses(List.of("2/BREED 9"))
            .nameAddresses(List.of("SADDAM HUSSEIN", "2/BREED 9"))
            .ctryTown("3/NL/1621KA HOORN NH")
            .build());
  }

  @Test
  void shouldExtractFedFormat() {
    extractOriginatorAlertedPartyData = new ExtractOriginatorAlertedPartyData(
        new MessageData(List.of(new MessageTag("ORIGINATOR", "NAME\n"
            + "ADDRESS\n"
            + "COUNTRYTOWN\n"
            + "US"))));
    assertThat(extractOriginatorAlertedPartyData.extract(
        MessageFieldStructure.NAMEADDRESS_FORMAT_F, MessageFormat.FED)).isEqualTo(
        AlertedPartyData.builder()
            .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_F)
            .name("NAME")
            .addresses(List.of("ADDRESS"))
            .nameAddresses(List.of("NAME", "ADDRESS"))
            .ctryTown("COUNTRYTOWN")
            .build());
  }

  @Test
  void shouldExtractLongFirstLine() {
    extractOriginatorAlertedPartyData = new ExtractOriginatorAlertedPartyData(
        new MessageData(List.of(new MessageTag("ORIGINATOR", "ACCOUNTNUMBER\n"
            + "NAME\n"
            + "ADDRESS\n"
            + "COUNTRYTOWN\n"
            + "US"))));
    assertThat(extractOriginatorAlertedPartyData.extract(
        MessageFieldStructure.NAMEADDRESS_FORMAT_F, MessageFormat.IATI)).isEqualTo(
        AlertedPartyData.builder()
            .messageFieldStructure(MessageFieldStructure.NAMEADDRESS_FORMAT_F)
            .accountNumber("ACCOUNTNUMBER")
            .name("NAME")
            .addresses(List.of("ADDRESS"))
            .nameAddresses(List.of("NAME", "ADDRESS"))
            .ctryTown("COUNTRYTOWN")
            .build());
  }
}
