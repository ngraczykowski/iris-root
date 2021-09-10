package com.silenteight.payments.bridge.agents.service;

import com.silenteight.payments.bridge.agents.model.ChineseCommercialCodesAgentResponse;
import com.silenteight.payments.bridge.agents.service.ChineseCommercialCodesAgent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChineseCommercialCodesAgentTest {

  private ChineseCommercialCodesAgent agent;

  @BeforeEach
  void beforeEach() {
    agent = new ChineseCommercialCodesAgent();
  }

  @Test
  void throwNullPointerExceptionWhenMessageDataIsNull() {
    assertThrows(NullPointerException.class, () -> agent.invoke(null));
  }

  @ParameterizedTest
  @CsvSource({
      "'',NO",
      "'/###\\nABC ELECTRONIC GROUP CO., LIMITED\\nADD. 2489 6037 3591 1681 5894 9\\n"
          + "5714 5281 4099 0794 2814 1129 1669\\n21 2869 01 1358 HK\\n',YES",
      "'/###\\n1438 7301\\n0006 3189 1579 7022 1337 0575 2450\\n5478 6424 393 1702 71"
          + " 5714 205 1358\\n/CHINA\\n        ',YES",
      "'/SPRO/12/DEPN/34/QUEU/N\\n/REMTCR/RETN/\\n/REC/RETN REASON:0008 4569 0678\\n//2398 4595 "
          + "6016 1353 , 6622 0565 .\\n// 6316 3938 3945 2974 4581 2099\\n//7110\\n         ',YES",
      "'\"/###\\n1202 7201 6721 3234 1101 0589\\n1472 0575 0467 3189 0467 3494\\n0001 6424 "
          + "EHUB 1125 1562 1034 A\\n1654 2 2869 1-234\\n',YES",
      "'/###\\n1313 3134\\n1639 2639 4164 0022 1472 1579 0657\\n6966 6966 2639 5887 0022"
          + " 6424 10\\n5714/CHINA\\n',YES",
      "'/###\\n1313 3134\\n1639 26329 4164 00222 1472 15792 0657\\n6966 69266 2639 "
          + "58287 0022 64224 10\\n5714/CHINA\\n',NO",
      "'/###\\n1313 3134\\n1639 269 4164 022 1472 152 0657\\n6966 692 2639 582 0022 "
          + "644 10\\n5714/CHINA\\n',NO"
  })
  void parameterizedTest(String messageData, ChineseCommercialCodesAgentResponse expected) {
    var actual = agent.invoke(messageData);
    assertEquals(expected, actual);
  }

}
