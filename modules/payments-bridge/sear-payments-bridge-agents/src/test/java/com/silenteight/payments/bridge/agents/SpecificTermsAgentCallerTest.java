package com.silenteight.payments.bridge.agents;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecificTermsAgentCallerTest {

  private SpecificTermsAgent specificTermsAgent;

  private static final List<String> SPECIFIC_TERMS =
      asList(
          "([12]/|\n|\\s)(D|S|W|Y)(/|\\s|-)?O([12]/|\n|\\s|\\b)",
          "(?i)([12]/|\n|\\s|,)P.?O.?([12]/|\n|\\s)?BOX([12]/|\n|\\s)",
          "([12]/|\n|\\s|,)P.?([12]/|\n|\\s)?B.([12]/|\n|\\s)?[0-9]{3,9}([12]/|\n|\\s|,)",
          "([12]/|\n|\\s|,)P.([12]/|\n|\\s)?B.([12]/|\n|\\s|,)",
          "([12]/|\n|\\s|,)PO([12]/|\n|\\s)?[0-9]{3,9}([12]/|\n|\\s|,)",
          "([12]/|\n|\\s|,)(PO|BOX)([12]/|\n|\\s)?[0-9]{1}",
          "([12]/|\n|\\s)C(/|\\s)O([12]/|\n|\\s|-)",
          "([12]/|\n|\\s)FORMALLY([12]/|\n|\\s)KNOWN([12]/|\n|\\s)AS([12]/|\n|\\s)",
          "([12]/|\n|\\s)ON([12]/|\n|\\s)BEHALF([12]/|\n|\\s)OF([12]/|\n|\\s)",
          "([12]/|\n|\\s)FOR([12]/|\n|\\s)FURTHER([12]/|\n|\\s)CREDIT([12]/|\n|\\s)",
          "([12]/|\n|\\s)ATT(ENTIO)?N([12]/|\n|\\s)?([12]/|\n|\\s|:)",
          "OWNED([12]/|\n|\\s)BY([12]/|\n|\\s)",
          "OFFICE([12]/|\n|\\s)FROM([12]/|\n|\\s)",
          "(MEMBER|OFFICE)([12]/|\n|\\s)OF([12]/|\n|\\s)",
          "([12]/|\n|\\s|\\b)(TRUST|ESTATE|FORMER?LY|LOUNGE)([12]/|\n|\\s|\\b)",
          "([12]/|\n|\\s)(T/AS?|O/B|EN/OF)([12]/|\n|\\s)"
          );

  @BeforeEach
  void beforeEach() {
    specificTermsAgent = new SpecificTermsAgent(SPECIFIC_TERMS);
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(NullPointerException.class, () -> specificTermsAgent.invoke(null));
  }

  @ParameterizedTest(name = "{index} {0}")
  @CsvSource({
      "'MOHD ALI\nY-O MR OSAMA BIN LADEN', YES",
      "'MOHD ALI\nW/O MR OSAMA BIN LADEN', YES",
      "'MOHD ALI WO OSAMA BIN LADEN', YES",
      "'EXECUTIVE LOUNGE AEROPUERTO INTERNACIONAL AIRPORT', YES",
      "'ABC PVT LTD\n (FORMELY INTERCONTINENTAL LIMITED),', YES",
      "'ABC PVT LTD\n (FORMERLY INTERCONTINENTAL LIMITED),', YES",
      "'MOHD ALI EN/OF OSAMA BIN LADEN', YES",
      "'BARRON ESTATE.JUDITH ', YES",
      "'TRUSTEE OF THE CASPIAN TRUST\r\n,', YES",
      "'DXB\nC/O-ABC PVT LTD,', YES",
      "'DXB,PO Box 31582,', YES",
      "'DXB\nBOX 7', YES",
      "'DXB\nPO7 023', YES",
      "'DXB\nP.B. 7', YES",
      "'OFFICE OF OSAMA BIN LADEN', YES",
      "'MOHD ALI SO OSAMA BIN LADEN', YES",
      "'MOHD ALI S/O:OSAMA BIN LADEN', YES",
      "'1/NAME1 NAME2 PO 123456789 /AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "' ,MEMBER OF ISIS,', YES",
      "' ,P.B. 42245,', YES",
      "' ,OWNED BY ISIS', YES",
      "' ,OFFICE FROM ISIS', YES",
      "' ,PO BOX 42245,', YES",
      "' \r\nPO 42245,', YES",
      "' ATTN:AFTAB ', YES",
      "'1/NAME1 NAME2 \nATTN TO: 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 ATTENTION TO 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 ATTN: 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 SO 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 S O 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 S/O 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 P.O 2/AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', NO",
      "'1/NAME1 NAME2 PO123 /AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'1/NAME1 NAME2 2/S/O AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "'ABC PVT LTD\n C/O INTERCONTINENTAL TRUST LIMITED,\n LEVEL 3, ALEXANDER HOUSE, "
          + "35\n CYBERCITY, EBENE, 72201, MAURITIUS', YES",
      "'1/NAME1 NAME2 2/PO 123 AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', YES",
      "' S/O\r\n', YES",
      "'ABC PVT LTD\n INTERCONTINENTAL TRUST LIMITED,\n LEVEL 3, ALEXANDER HOUSE, "
          + "35\n CYBERCITY, EBENE, 72201, MAURITIUS', YES",
      "'1/NAME1 NAME2 2//O AFTAB AHMAD Q M AMROHA_x000D_ 3/CTRYTOWN', NO",
      "' S O BANK OF SCOTLAND PLC\n', YES"
  })
  void parametrizedTest(String fieldValue, SpecificTermsAgentResponse expected) {
    SpecificTermsAgentResponse actual = specificTermsAgent.invoke(fieldValue);
    assertEquals(expected, actual);
  }


}
