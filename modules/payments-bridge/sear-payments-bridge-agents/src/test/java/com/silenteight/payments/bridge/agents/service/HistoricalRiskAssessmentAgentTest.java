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
            "classpath:historical-risk-assessment-agent/SVB.csv");
    historicalRiskAssessmentAgent = new HistoricalRiskAssessmentAgent(configTuples);
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(NullPointerException.class, () ->
        historicalRiskAssessmentAgent.invoke(null));
  }

  @ParameterizedTest
  @CsvSource({
      "'DAVID L YANKEE 2015 TRUST','AS00692456   ', 'YES'",
      "'DAVID L YANKEE 2015 TRUST','', 'NO'",
      "'CHINA UNICORN (EUROPE) OPERATIONS LIMITED','as06331366\n', 'YES'",
      "'NIS A.D. NOVI SAD','sizada4224', 'NO'",
      "'A.S.B.FV\n','SIBFMX', 'YES'",
      "'A.S.B.fv','SIBFMX', 'YES'",
      "'CHINA UNICORN \n(EUROPE) OPERATIONS LIMITED','AS06331366\n\n', 'NO'",
      "'','', 'NO'"
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
