package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.service.HistoricalRiskAssessmentAgent.ConfigTuple;
import com.silenteight.payments.bridge.agents.service.decoder.DecodedResourceLoader;
import com.silenteight.payments.bridge.agents.service.decoder.DecoderModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HistoricalRiskAssessmentAgentTest.class)
@ComponentScan(basePackageClasses = DecoderModule.class)
class HistoricalRiskAssessmentAgentTest {

  private HistoricalRiskAssessmentAgent historicalRiskAssessmentAgent;

  @Autowired
  DecodedResourceLoader decodedResourceLoader;

  @BeforeEach
  void beforeEach() throws IOException {
    List<ConfigTuple> configTuples =
        HistoricalRiskAssessmentBlackListCsvReader.getConfigListFromCsv(
            decodedResourceLoader,
            "classpath:historical-risk-assessment-agent/list-1.csv");
    historicalRiskAssessmentAgent = new HistoricalRiskAssessmentAgent(configTuples);
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(NullPointerException.class, () ->
        historicalRiskAssessmentAgent.invoke(null));
  }

  @ParameterizedTest
  @CsvSource({
      "'BANK OF SCOTLAND PLC','D0036701908273410   ', 'YES'",
      "'TRIDENT TECHNOLOGIES LIMITED','00001', 'YES'",
      "'TRIDENT TECHNOLOGIES LIMITED \n','00001', 'YES'",
      "'TRIDENT TECHNOLOGIES LIMITED vvv','00001', 'NO'",
      "'J STAR','  000s01    ','NO'",
      "'','     ', 'YES'",
      "'NYNAS OYccc','    _    ', 'NO'",
      "'SALEH AL-SAADI, NESSIM BEN MOHAMED ','true false true  ', 'NO'",
      "' MTM SHIP MANAGEMENT PTE LTD','  KW 74 NBOK 0000 0000 0000 1000 3721 51   ','YES'",
      "' Premier Bank Intl','', 'YES'",
      "' Premier Bank Intl','     ', 'YES'",
      "' Premier Bank Intl','  \n     ', 'YES'",
      "'HYUNDAI MERCHANT MARINE (U.A.E.) L.L.C','12345678(ABC)   ', 'YES'",
      "'NINGBO TWO BIRDS INDUSTRY COMPANY LIMITED','          ','YES'",
      "'BANQUE R SUPERVIELLE ET FILS S.A.',' |   |  ', 'YES'",
      "'BUY4EASY INCc','    D0036701908273410   ', 'NO'",
      "'12345678(ABC)','000\n01', 'NO'",
      "'AL47212110090000000235698741','0\r\n0001','NO'",
      "'AD1200012030200359100100','00\n00\r\n1\n|\n','NO'",
      "'', '  ','YES'",
      "'AT6119043002 3457 3201', '  GY&%UYGI', 'YES'",
      "'SALEH AL-SAADI, NESSIM BEN MOHAMED AL-CHERIF BEN MOHAMED','mmmmMMM', 'NO'",
      "'MTM SHIP MANAGEMENT PTE LTD','~~~~§', 'NO'",
      "'Premier Bank Intll','£§£§  {{', 'NO'",
      "'BE 68 5390 0754 7034', '....,,,  ', 'YES'",
      "'IT 60 X054 2811 1010 0000 0123 456 ','00002jddw', 'YES'",
      "'12345678(ABC) ', 'fde\ns', 'NO'",
      "'12345678(ABC) ', 'fde\n\r\ns', 'NO'",
      "'null null   null  ','a.c.vMyClaass', 'YES'",
      "'036701908273410', 'return', 'NO'",
      "'true false true false', '   ss_  ', 'YES'",
      "'KW 74 NBOK 0000 0000 0000 1000 3721 51 ','', 'YES'",
      "'KW 74 NBOK 0000 0000 0000 1000 3721 51 ','         ', 'YES'",
      "'', '', 'YES'",
      "'         \n',' '1111', 'NO'",
      "'_','1111', 'YES'",
      "'SALEH AL-SAADI, NESSIM BEN MOHAMED AL-CHERIF BEN MOHAME','[1 - 9]', 'NO'",
      "'MTM SHIP MANAGEMENT PTE LTD',' TEST tstest', 'YES'",
      "'PL 27 1140 2004 0000 3002 0135 5387)))',' 999998v777hdhbeBB', 'NO'",
      "'HYUNDAI MERCHANT MARINE (U.A.E.) L.L.C;','???!!/???', 'NO'",
      "'  \r\n\n','   \n\n\n  ','YES'",
      "'*gf*hgg**g*gg***','   \n\n\n  ','NO'",
      "'sqiwdg 77whhw 2092j .,,','&&&&&h&', 'NO'",
      "'NL 91 ABNA 0417 1643 00','D0036701908273410  ','YES'",
      "'NL 91 ABNA 0417 1643 00',  'TEST tstest', 'NO'"
  })
  void parametrizedTest(String accountNumber, String ofactId, String expected) {
    var historicalRiskAssessmentAgentRequest = HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(accountNumber)
        .ofacID(ofactId)
        .build();
    var actual = historicalRiskAssessmentAgent.invoke(historicalRiskAssessmentAgentRequest);
    assertEquals(expected, actual.name());
  }
}
